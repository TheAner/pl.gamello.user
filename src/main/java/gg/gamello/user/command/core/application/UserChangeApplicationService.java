package gg.gamello.user.command.core.application;

import gg.gamello.user.command.avatar.AvatarService;
import gg.gamello.user.command.avatar.exception.AvatarException;
import gg.gamello.user.command.confirmation.aplication.command.CreateCommand;
import gg.gamello.user.command.confirmation.domain.action.ActionType;
import gg.gamello.user.command.confirmation.domain.method.MethodType;
import gg.gamello.user.command.core.application.command.*;
import gg.gamello.user.query.core.application.dto.UserDtoAssembler;
import gg.gamello.user.command.core.domain.User;
import gg.gamello.user.command.core.domain.UserFactory;
import gg.gamello.user.command.core.domain.UserRepository;
import gg.gamello.user.command.core.domain.confirmation.Confirmation;
import gg.gamello.user.command.core.infrastructure.exception.PropertyConflictException;
import gg.gamello.user.command.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.command.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.command.core.infrastructure.exception.UserIsNotActiveException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
public class UserChangeApplicationService {

	private final UserFactory userFactory;
	private final UserRepository userRepository;
	private final Confirmation confirmation;
	private final AvatarService avatarService;

	public UserChangeApplicationService(UserFactory userFactory, UserRepository userRepository,
										Confirmation confirmation, AvatarService avatarService) {
		this.userFactory = userFactory;
		this.userRepository = userRepository;
		this.confirmation = confirmation;
		this.avatarService = avatarService;
	}

	@Transactional
	public UUID requestRegister(RegisterCommand command) throws UserAlreadyExistsException {
		if (userRepository.existsUserByEmailOrUsername(command.getEmail(), command.getUsername()))
			throw new UserAlreadyExistsException("User with credentials " +
					command.getEmail() + "/" + command.getUsername() + " already exists");
		User user = userFactory.create(command);

		var confirmationRequest = CreateCommand.builder()
				.user(UserDtoAssembler.convertDetailed(user))
				.action(ActionType.ACTIVATION)
				.method(MethodType.EMAIL)
				.build();
		confirmation.request(confirmationRequest);

		return userRepository.save(user).getId();
	}

	public void requestDelete(AuthenticationContainer container) {
		User user = find(container);
		var confirmationRequest = CreateCommand.builder()
				.user(UserDtoAssembler.convertDetailed(user))
				.action(ActionType.DELETE)
				.method(MethodType.EMAIL)
				.build();

		confirmation.request(confirmationRequest);
	}

	public void requestRecover(RecoverRequestCommand command) {
		try {
			User user = find(command.getEmail());
			user.checkActive();
			var confirmationRequest = CreateCommand.builder()
					.user(UserDtoAssembler.convertDetailed(user))
					.action(ActionType.PASSWORD)
					.method(MethodType.EMAIL)
					.build();

			confirmation.request(confirmationRequest);

		} catch (UserDoesNotExistsException | UserIsNotActiveException e) {
			log.error(e.getMessage());
		}
	}

	public void requestEmailChange(AuthenticationContainer container, EmailChangeRequestCommand command) throws PropertyConflictException {
		User user = find(container);
		if (user.getEmail().equals(command.getEmail()))
			throw new PropertyConflictException("email", "Given email is same as existing one");

		var confirmationRequest = CreateCommand.builder()
				.user(UserDtoAssembler.convertDetailed(user))
				.action(ActionType.EMAIL)
				.method(MethodType.EMAIL)
				.attachment(command.getEmail())
				.build();

		confirmation.request(confirmationRequest);
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

	private User find(String email) throws UserDoesNotExistsException {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UserDoesNotExistsException(email, "User does not exists"));
	}

	private User find(AuthenticationContainer container) {
		return userRepository.findById(container.getUser().getId())
				.orElseThrow(() -> new IllegalStateException("User from authentication does not exists"));
	}
}
