package gg.gamello.user.core.application;

import gg.gamello.user.confirmation.aplication.command.CreateCommand;
import gg.gamello.user.confirmation.domain.action.ActionType;
import gg.gamello.user.confirmation.domain.method.MethodType;
import gg.gamello.user.core.application.command.EmailChangeRequestCommand;
import gg.gamello.user.core.application.dto.UserDtoAssembler;
import gg.gamello.user.core.domain.User;
import gg.gamello.user.core.domain.UserRepository;
import gg.gamello.user.core.domain.confirmation.Confirmation;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationUser;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserApplicationService {

	private UserRepository userRepository;
	private Confirmation confirmation;

	public UserApplicationService(UserRepository userRepository, Confirmation confirmation) {
		this.userRepository = userRepository;
		this.confirmation = confirmation;
	}

	public void createDeleteRequest(AuthenticationUser authenticationUser) {
		User user = findUser(authenticationUser);
		var confirmationRequest = CreateCommand.builder()
				.user(UserDtoAssembler.convertDefault(user))
				.action(ActionType.DELETE)
				.method(MethodType.EMAIL)
				.build();

		confirmation.request(confirmationRequest);
	}

	public void createRecoverRequest(AuthenticationUser authenticationUser) {
		User user = findUser(authenticationUser);
		var confirmationRequest = CreateCommand.builder()
				.user(UserDtoAssembler.convertDefault(user))
				.action(ActionType.PASSWORD)
				.method(MethodType.EMAIL)
				.build();

		confirmation.request(confirmationRequest);
	}

	public void createEmailChangeRequest(AuthenticationUser authenticationUser, EmailChangeRequestCommand command) {
		User user = findUser(authenticationUser);
		var confirmationRequest = CreateCommand.builder()
				.user(UserDtoAssembler.convertDefault(user))
				.action(ActionType.EMAIL)
				.method(MethodType.EMAIL)
				.attachment(command.getEmail())
				.build();

		confirmation.request(confirmationRequest);
	}

	private User findUser(UUID userId) throws UserDoesNotExistsException {
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserDoesNotExistsException(userId.toString(), "User does not exists"));
	}

	private User findUser(AuthenticationUser user) {
		return userRepository.findById(user.getId())
				.orElseThrow(() -> new IllegalStateException("User from authentication does not exists"));
	}
}
