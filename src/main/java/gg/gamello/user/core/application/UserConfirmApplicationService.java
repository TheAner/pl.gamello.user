package gg.gamello.user.core.application;

import gg.gamello.user.confirmation.aplication.command.ConfirmationCommand;
import gg.gamello.user.confirmation.domain.action.ActionType;
import gg.gamello.user.confirmation.infrastructure.exception.ConfirmationDoesNotExistsException;
import gg.gamello.user.confirmation.infrastructure.exception.ConfirmationException;
import gg.gamello.user.confirmation.infrastructure.exception.IncorrectSecretException;
import gg.gamello.user.confirmation.infrastructure.exception.OutdatedConfirmationException;
import gg.gamello.user.confirmation.infrastructure.provider.email.EmailProvider;
import gg.gamello.user.core.application.command.*;
import gg.gamello.user.core.application.dto.UserDto;
import gg.gamello.user.core.application.dto.UserDtoAssembler;
import gg.gamello.user.core.domain.User;
import gg.gamello.user.core.domain.UserRepository;
import gg.gamello.user.core.domain.confirmation.Confirmation;
import gg.gamello.user.core.infrastructure.exception.PasswordsDontMatchException;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.core.infrastructure.exception.UserIsNotActiveException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class UserConfirmApplicationService {

	private final UserRepository userRepository;
	private final PasswordEncoder encoder;
	private final Confirmation confirmation;
	private final EmailProvider emailProvider;
	private final HttpServletRequest httpRequest;

	public UserConfirmApplicationService(UserRepository userRepository, PasswordEncoder encoder,
										 Confirmation confirmation, EmailProvider emailProvider,
										 HttpServletRequest httpRequest) {
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.confirmation = confirmation;
		this.emailProvider = emailProvider;
		this.httpRequest = httpRequest;
	}

	@Transactional
	public void activate(ConfirmCommand command) throws UserDoesNotExistsException, ConfirmationException {
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
	public void delete(ConfirmCommand command) throws UserDoesNotExistsException, ConfirmationException {
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
	public void recover(RecoverConfirmCommand command) throws UserDoesNotExistsException, ConfirmationException {
		User user = find(command.getUserId());

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
	public void changePassword(AuthenticationContainer container, PasswordChangeCommand command) throws PasswordsDontMatchException {
		User user = find(container);
		user.matchPassword(command.getOldPassword(), command.getNewPassword(), encoder);

		var message = emailProvider.messageBuilder()
				.user(user.getId(), user.getUsername(), user.getEmail())
				.language(user.getLanguage())
				.issuer(httpRequest.getRemoteUser())
				.useTemplateChanged(ActionType.PASSWORD)
				.build();
		emailProvider.send(message);

		user.changePassword(command.getNewPassword(), encoder);
		userRepository.save(user);
	}

	@Transactional
	public void changeEmail(ConfirmCommand command) throws UserDoesNotExistsException, ConfirmationException {
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
				.issuer(httpRequest.getRemoteUser())
				.useTemplateChanged(ActionType.EMAIL)
				.build();
		emailProvider.send(message);

		user.changeEmail(newEmail);
		userRepository.save(user);
	}

	public void checkSecret(CheckSecretCommand command)
			throws IncorrectSecretException, ConfirmationDoesNotExistsException, OutdatedConfirmationException {
		var confirmationCommand = ConfirmationCommand.builder()
				.userId(command.getUserId())
				.secret(command.getSecret())
				.action(ActionType.valueOf(command.getAction()))
				.build();

		confirmation.check(confirmationCommand);
	}

	public UserDto authenticateCredentials(CredentialsCommand command) throws UserDoesNotExistsException, UserIsNotActiveException {
		User user = userRepository.findByCredentials(command.getLogin())
				.orElseThrow(() -> new UserDoesNotExistsException(command.getLogin(), "User does not exists"));

		user.checkActive();

		if (!encoder.matches(command.getPassword(), user.getPassword()))
			throw new UserDoesNotExistsException(command.getLogin(), "User does not exists");

		return UserDtoAssembler.convertWithRoles(user);
	}

	private User find(UUID userId) throws UserDoesNotExistsException {
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserDoesNotExistsException(userId.toString(), "User does not exists"));
	}

	private User find(AuthenticationContainer container) {
		return userRepository.findById(container.getUser().getId())
				.orElseThrow(() -> new IllegalStateException("User from authentication does not exists"));
	}
}
