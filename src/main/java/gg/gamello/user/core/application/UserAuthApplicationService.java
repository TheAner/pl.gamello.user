package gg.gamello.user.core.application;

import gg.gamello.user.confirmation.aplication.command.ConfirmationCommand;
import gg.gamello.user.confirmation.aplication.command.CreateCommand;
import gg.gamello.user.confirmation.domain.action.ActionType;
import gg.gamello.user.confirmation.domain.method.MethodType;
import gg.gamello.user.confirmation.infrastructure.exception.ConfirmationException;
import gg.gamello.user.confirmation.infrastructure.provider.email.EmailProvider;
import gg.gamello.user.core.application.command.*;
import gg.gamello.user.core.application.dto.UserDtoAssembler;
import gg.gamello.user.core.domain.User;
import gg.gamello.user.core.domain.UserFactory;
import gg.gamello.user.core.domain.UserRepository;
import gg.gamello.user.core.domain.confirmation.Confirmation;
import gg.gamello.user.core.infrastructure.exception.PasswordsDontMatchException;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.core.infrastructure.exception.UserIsNotActiveException;
import gg.gamello.user.infrastructure.security.AuthenticationUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserAuthApplicationService {

	private UserFactory userFactory;

	private UserRepository userRepository;

	private final PasswordEncoder encoder;
	private final Confirmation confirmation;
	private final EmailProvider emailProvider;

	public UserAuthApplicationService(UserFactory userFactory, UserRepository userRepository,
									  PasswordEncoder encoder, Confirmation confirmation,
									  EmailProvider emailProvider) {
		this.userFactory = userFactory;
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.confirmation = confirmation;
		this.emailProvider = emailProvider;
	}

	@Transactional
	public User create(RegisterCommand command) throws UserAlreadyExistsException {
		if (userRepository.existsUserByEmailOrUsername(command.getEmail(), command.getUsername()))
			throw new UserAlreadyExistsException("User with credentials "  +
					command.getEmail() + "/" + command.getUsername() + " already exists");
		User user = userFactory.create(command);

		var confirmationRequest = CreateCommand.builder()
				.user(UserDtoAssembler.convertDefault(user))
				.action(ActionType.ACTIVATION)
				.method(MethodType.EMAIL)
				.build();
		confirmation.request(confirmationRequest);

		return userRepository.save(user);
	}

	@Transactional
	public void activate(ActivateCommand command) throws UserDoesNotExistsException, ConfirmationException {
		User user = find(command.getUserId());

		var confirmationCommand = ConfirmationCommand.builder()
				.userId(user.getId())
				.action(ActionType.ACTIVATION)
				.secret(command.getSecret())
				.build();
		confirmation.validate(confirmationCommand);

		user.activate();
		userRepository.save(user);
	}

	@Transactional
	public void delete(DeleteCommand command) throws UserDoesNotExistsException, ConfirmationException {
		User user = find(command.getUserId());

		var confirmationCommand = ConfirmationCommand.builder()
				.userId(user.getId())
				.action(ActionType.DELETE)
				.secret(command.getSecret())
				.build();
		confirmation.validate(confirmationCommand);

		userRepository.delete(user);
	}

	@Transactional
	public void recover(RecoverCommand command) throws UserDoesNotExistsException, UserIsNotActiveException, ConfirmationException {
		User user = find(command.getUserId());
		user.checkActive();

		var confirmationCommand = ConfirmationCommand.builder()
				.userId(user.getId())
				.action(ActionType.PASSWORD)
				.secret(command.getSecret())
				.build();
		confirmation.validate(confirmationCommand);

		user.changePassword(command.getPassword(), encoder);
		userRepository.save(user);
	}

	@Transactional
	public void changePassword(AuthenticationUser authenticationUser, PasswordChangeCommand command) throws PasswordsDontMatchException {
		User user = find(authenticationUser);
		user.matchPassword(command.getOldPassword(), command.getNewPassword(), encoder);

		var message = emailProvider.messageBuilder()
				.user(user.getId(), user.getUsername(), user.getEmail())
				.language(user.getLanguage())
				.useTemplateChanged(ActionType.PASSWORD)
				.build();
		emailProvider.send(message);

		user.changePassword(command.getNewPassword(), encoder);
		userRepository.save(user);
	}

	@Transactional
	public void changeEmail(EmailChangeCommand command) throws UserDoesNotExistsException, ConfirmationException {
		User user = find(command.getUserId());

		var confirmationCommand = ConfirmationCommand.builder()
				.userId(user.getId())
				.action(ActionType.EMAIL)
				.secret(command.getSecret())
				.build();
		String newEmail = confirmation.validate(confirmationCommand)
				.orElse(user.getEmail());

		var message = emailProvider.messageBuilder()
				.user(user.getId(), user.getUsername(), user.getEmail())
				.language(user.getLanguage())
				.useTemplateChanged(ActionType.EMAIL)
				.build();
		emailProvider.send(message);

		user.changeEmail(newEmail);
		userRepository.save(user);
	}

	private User find(UUID userId) throws UserDoesNotExistsException {
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserDoesNotExistsException(userId.toString(), "User does not exists"));
	}

	private User find(AuthenticationUser user) {
		return userRepository.findById(user.getId())
				.orElseThrow(() -> new IllegalStateException("User from authentication does not exists"));
	}
}
