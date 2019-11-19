package gg.gamello.user.core.application;

import gg.gamello.user.core.application.dto.UserDto;
import gg.gamello.user.core.application.dto.UserDtoAssembler;
import gg.gamello.user.core.domain.User;
import gg.gamello.user.core.domain.UserRepository;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationService { //todo: delete

	private final UserRepository userRepository;

	public UserApplicationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserDto getLogged(AuthenticationContainer container) {
		return UserDtoAssembler.convertDefault(find(container));
	}

	public UserDto getBySlug(String slug) throws UserDoesNotExistsException {
		return UserDtoAssembler.convertDefault(find(slug));
	}

	private User find(String slug) throws UserDoesNotExistsException {
		return userRepository.findBySlug(slug)
				.orElseThrow(() -> new UserDoesNotExistsException(slug, "User does not exists"));
	}

	private User find(AuthenticationContainer container) {
		return userRepository.findById(container.getUser().getId())
				.orElseThrow(() -> new IllegalStateException("User from authentication does not exists"));
	}
}
