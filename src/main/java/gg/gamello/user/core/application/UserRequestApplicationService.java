package gg.gamello.user.core.application;

import gg.gamello.user.confirmation.aplication.command.CreateCommand;
import gg.gamello.user.confirmation.domain.action.ActionType;
import gg.gamello.user.confirmation.domain.method.MethodType;
import gg.gamello.user.core.application.command.EmailChangeRequestCommand;
import gg.gamello.user.core.application.command.RecoverRequestCommand;
import gg.gamello.user.core.application.command.RegisterCommand;
import gg.gamello.user.core.application.dto.UserDtoAssembler;
import gg.gamello.user.core.domain.User;
import gg.gamello.user.core.domain.UserFactory;
import gg.gamello.user.core.domain.UserRepository;
import gg.gamello.user.core.domain.confirmation.Confirmation;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.core.infrastructure.exception.UserIsNotActiveException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class UserRequestApplicationService {

	private final UserFactory userFactory;
	private final UserRepository userRepository;
	private final Confirmation confirmation;

	public UserRequestApplicationService(UserFactory userFactory, UserRepository userRepository,
										 Confirmation confirmation) {
		this.userFactory = userFactory;
		this.userRepository = userRepository;
		this.confirmation = confirmation;
	}

	@Transactional
	public UUID create(RegisterCommand command) throws UserAlreadyExistsException {
		if (userRepository.existsUserByEmailOrUsername(command.getEmail(), command.getUsername()))
			throw new UserAlreadyExistsException("User with credentials " +
					command.getEmail() + "/" + command.getUsername() + " already exists");
		User user = userFactory.create(command);

		var confirmationRequest = CreateCommand.builder()
				.user(UserDtoAssembler.convertDefault(user))
				.action(ActionType.ACTIVATION)
				.method(MethodType.EMAIL)
				.build();
		confirmation.request(confirmationRequest);

		return userRepository.save(user).getId();
	}

	public void createDeleteRequest(AuthenticationContainer container) {
		User user = findUser(container);
		var confirmationRequest = CreateCommand.builder()
				.user(UserDtoAssembler.convertDefault(user))
				.action(ActionType.DELETE)
				.method(MethodType.EMAIL)
				.build();

		confirmation.request(confirmationRequest);
	}

	public void createRecoverRequest(RecoverRequestCommand command) {
		try {
			User user = findUser(command.getEmail());
			user.checkActive();
			var confirmationRequest = CreateCommand.builder()
					.user(UserDtoAssembler.convertDefault(user))
					.action(ActionType.PASSWORD)
					.method(MethodType.EMAIL)
					.build();

			confirmation.request(confirmationRequest);

		} catch (UserDoesNotExistsException | UserIsNotActiveException e) {
			log.error(e.getMessage());
		}
	}

	public void createEmailChangeRequest(AuthenticationContainer container, EmailChangeRequestCommand command) {
		User user = findUser(container);
		var confirmationRequest = CreateCommand.builder()
				.user(UserDtoAssembler.convertDefault(user))
				.action(ActionType.EMAIL)
				.method(MethodType.EMAIL)
				.attachment(command.getEmail())
				.build();

		confirmation.request(confirmationRequest);
	}

	private User findUser(String email) throws UserDoesNotExistsException {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UserDoesNotExistsException(email, "User does not exists"));
	}

	private User findUser(AuthenticationContainer container) {
		return userRepository.findById(container.getUser().getId())
				.orElseThrow(() -> new IllegalStateException("User from authentication does not exists"));
	}
}
