package gg.gamello.user.core.application;

import gg.gamello.user.core.application.dto.UserDto;
import gg.gamello.user.core.application.dto.UserDtoAssembler;
import gg.gamello.user.core.domain.User;
import gg.gamello.user.core.domain.UserRepository;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationUser;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserApplicationService {

	private final UserRepository userRepository;

	public UserApplicationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserDto getLoggedUser(AuthenticationUser user) {
		return UserDtoAssembler.convertDefault(find(user));
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
