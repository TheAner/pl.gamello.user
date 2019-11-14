package gg.gamello.user.core.application;

import gg.gamello.user.avatar.AvatarService;
import gg.gamello.user.avatar.exception.AvatarException;
import gg.gamello.user.core.application.command.LanguageChangeCommand;
import gg.gamello.user.core.application.command.SlugChangeCommand;
import gg.gamello.user.core.application.command.VisibleNameChangeCommand;
import gg.gamello.user.core.application.dto.UserDto;
import gg.gamello.user.core.application.dto.UserDtoAssembler;
import gg.gamello.user.core.domain.User;
import gg.gamello.user.core.domain.UserRepository;
import gg.gamello.user.core.infrastructure.exception.PropertyConflictException;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserApplicationService {

	private final UserRepository userRepository;
	private final AvatarService avatarService;

	public UserApplicationService(UserRepository userRepository, AvatarService avatarService) {
		this.userRepository = userRepository;
		this.avatarService = avatarService;
	}

	public UserDto getLoggedUser(AuthenticationContainer container) {
		return UserDtoAssembler.convertDefault(find(container));
	}

	public UserDto getUserBySlug(String slug) throws UserDoesNotExistsException {
		return UserDtoAssembler.convertDefault(find(slug));
	}

	public void changeLanguage(AuthenticationContainer container, LanguageChangeCommand command) {
		User user = find(container);
		user.changeLanguage(command.getLanguage());
		userRepository.save(user);
	}

	public void changeSlug(AuthenticationContainer container, SlugChangeCommand command) throws PropertyConflictException {
		User user = find(container);
		if (user.getSlug() != null && user.getSlug().equals(command.getSlug()))
			throw new PropertyConflictException("slug", "Given slug is same as existing one");

		if (userRepository.existsBySlug(command.getSlug())) {
			throw new UserAlreadyExistsException("User with slug " +
					command.getSlug() + " already exists");
		}
		user.changeSlug(command.getSlug());
		userRepository.save(user);
	}

	public void changeVisibleName(AuthenticationContainer container, VisibleNameChangeCommand command) throws PropertyConflictException {
		User user = find(container);
		if (user.getVisibleName().equals(command.getVisibleName()))
			throw new PropertyConflictException("visibleName", "Given visibleName is same as existing one");

		user.changeVisibleName(command.getVisibleName());
		userRepository.save(user);
	}

	public void changeAvatar(AuthenticationContainer container, MultipartFile image) throws AvatarException, InterruptedException {
		User user = find(container);

		var avatars = avatarService.generateAvatars(image);
		var location = avatarService.uploadListOfAvatars(avatars, user.getUsername());
		avatarService.deleteAvatarsInLocation(user.getAvatarLocation());

		user.changeAvatarLocation(location);
		userRepository.save(user);
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
