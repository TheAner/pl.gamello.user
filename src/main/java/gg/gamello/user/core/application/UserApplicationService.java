package gg.gamello.user.core.application;

import gg.gamello.user.core.application.command.EmailChangeRequestCommand;
import gg.gamello.user.core.domain.User;
import gg.gamello.user.core.domain.UserRepository;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationUser;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserApplicationService {

	private UserRepository userRepository;

	public UserApplicationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void createDeleteRequest(AuthenticationUser authenticationUser) {
		User user = findUser(authenticationUser);
		//todo: create delete confirmation
	}

	public void createRecoverRequest(AuthenticationUser authenticationUser) {
		User user = findUser(authenticationUser);
		//todo: create recover confirmation
	}

	public void createEmailChangeRequest(AuthenticationUser authenticationUser, EmailChangeRequestCommand command) {
		User user = findUser(authenticationUser);
		//todo: create email change confirmation
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
