package gg.gamello.user.core.application;

import gg.gamello.user.core.application.command.LanguageChangeCommand;
import gg.gamello.user.core.application.command.SlugChangeCommand;
import gg.gamello.user.core.application.command.VisibleNameChangeCommand;
import gg.gamello.user.core.application.dto.UserDto;
import gg.gamello.user.core.application.dto.UserDtoAssembler;
import gg.gamello.user.core.domain.User;
import gg.gamello.user.core.domain.UserRepository;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationUser;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationService {

	private final UserRepository userRepository;

	public UserApplicationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserDto getLoggedUser(AuthenticationUser user) {
		return UserDtoAssembler.convertDefault(find(user));
	}

	public UserDto getUserBySlug(String slug) throws UserDoesNotExistsException {
		return UserDtoAssembler.convertDefault(find(slug));
	}

	public void changeLanguage(AuthenticationUser authenticationUser, LanguageChangeCommand command) {
		User user = find(authenticationUser);
		user.changeLanguage(command.getLanguage());
		userRepository.save(user);
	}

	public void changeSlug(AuthenticationUser authenticationUser, SlugChangeCommand command) throws UserAlreadyExistsException {
		User user = find(authenticationUser);
		if (userRepository.existsBySlug(command.getSlug())) {
			throw new UserAlreadyExistsException("User with slug " +
					command.getSlug() + " already exists");
		}
		user.changeSlug(command.getSlug());
		userRepository.save(user);
	}

	public void changeVisibleName(AuthenticationUser authenticationUser, VisibleNameChangeCommand command) {
		User user = find(authenticationUser);
		user.changeVisibleName(command.getVisibleName());
		userRepository.save(user);
	}

	private User find(String slug) throws UserDoesNotExistsException {
		return userRepository.findBySlug(slug)
				.orElseThrow(() -> new UserDoesNotExistsException(slug, "User does not exists"));
	}

	private User find(AuthenticationUser user) {
		return userRepository.findById(user.getId())
				.orElseThrow(() -> new IllegalStateException("User from authentication does not exists"));
	}
}
