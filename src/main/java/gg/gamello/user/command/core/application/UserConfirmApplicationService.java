package gg.gamello.user.command.core.application;

import gg.gamello.user.command.confirmation.aplication.command.ConfirmationCommand;
import gg.gamello.user.command.confirmation.domain.action.ActionType;
import gg.gamello.user.command.confirmation.infrastructure.exception.ConfirmationException;
import gg.gamello.user.command.confirmation.infrastructure.provider.email.EmailProvider;
import gg.gamello.user.command.core.application.command.ConfirmCommand;
import gg.gamello.user.command.core.application.command.PasswordChangeCommand;
import gg.gamello.user.command.core.application.command.RecoverConfirmCommand;
import gg.gamello.user.command.core.domain.User;
import gg.gamello.user.command.core.domain.UserRepository;
import gg.gamello.user.command.core.domain.confirmation.Confirmation;
import gg.gamello.user.command.core.infrastructure.exception.PasswordsDontMatchException;
import gg.gamello.user.command.core.infrastructure.exception.PropertyConflictException;
import gg.gamello.user.command.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class UserConfirmApplicationService {

	private final UserRepository userRepository;
	private final PasswordEncoder encoder;
	private final Confirmation confirmation;
	private final EmailProvider emailProvider;
	private final HttpServletRequest httpRequest;
	private final CacheManager cacheManager;

	@Resource
	private UserConfirmApplicationService self;

	public UserConfirmApplicationService(UserRepository userRepository, PasswordEncoder encoder,
										 Confirmation confirmation, EmailProvider emailProvider,
										 HttpServletRequest httpRequest, CacheManager cacheManager) {
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.confirmation = confirmation;
		this.emailProvider = emailProvider;
		this.httpRequest = httpRequest;
		this.cacheManager = cacheManager;
	}

	@Transactional
	public void activate(ConfirmCommand command) throws UserDoesNotExistsException, ConfirmationException {
		User user = self.find(command.getUserId());

		var confirmationCommand = ConfirmationCommand.builder()
				.userId(user.getId())
				.action(ActionType.ACTIVATION)
				.secret(command.getSecret())
				.build();
		confirmation.validate(confirmationCommand);

		user.activate();
		userRepository.save(user);
		if (cacheManager.getCacheNames().contains("users")){
			cacheManager.getCache("users").put(user.getId(), user);
		}
	}

	@Transactional
	public void delete(ConfirmCommand command) throws UserDoesNotExistsException, ConfirmationException {
		User user = self.find(command.getUserId());

		var confirmationCommand = ConfirmationCommand.builder()
				.userId(user.getId())
				.action(ActionType.DELETE)
				.secret(command.getSecret())
				.build();
		confirmation.validate(confirmationCommand);

		userRepository.delete(user);
		if (cacheManager.getCacheNames().contains("users")){
			cacheManager.getCache("users").evict(user.getId());
			cacheManager.getCache("users").evict(user.getSlug());
		}
	}

	@Transactional
	public void recover(RecoverConfirmCommand command) throws UserDoesNotExistsException, ConfirmationException {
		User user = self.find(command.getUserId());

		var confirmationCommand = ConfirmationCommand.builder()
				.userId(user.getId())
				.action(ActionType.PASSWORD)
				.secret(command.getSecret())
				.build();
		confirmation.validate(confirmationCommand);

		user.changePassword(command.getPassword(), encoder);
		userRepository.save(user);
		if (cacheManager.getCacheNames().contains("users")){
			cacheManager.getCache("users").put(user.getId(), user);
		}
	}

	@Transactional
	public void changePassword(AuthenticationContainer container, PasswordChangeCommand command) throws PasswordsDontMatchException, PropertyConflictException {
		User user = self.find(container);
		if (command.getOldPassword().equals(command.getNewPassword()))
			throw new PropertyConflictException("passwordChangeCommand", "Given passwords are same");

		user.matchPassword(command.getOldPassword(), encoder);

		var message = emailProvider.messageBuilder()
				.user(user.getId(), user.getUsername(), user.getEmail())
				.language(user.getLanguage())
				.withIssuer(getRemoteAddress(httpRequest))
				.useTemplateChanged(ActionType.PASSWORD)
				.build();
		emailProvider.send(message);

		user.changePassword(command.getNewPassword(), encoder);
		userRepository.save(user);
		if (cacheManager.getCacheNames().contains("users")){
			cacheManager.getCache("users").put(user.getId(), user);
		}
	}

	@Transactional
	public void changeEmail(ConfirmCommand command) throws UserDoesNotExistsException, ConfirmationException {
		User user = self.find(command.getUserId());

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
				.withIssuer(getRemoteAddress(httpRequest))
				.useTemplateChanged(ActionType.EMAIL)
				.addData("newEmail", newEmail)
				.build();
		emailProvider.send(message);

		user.changeEmail(newEmail);
		userRepository.save(user);
		if (cacheManager.getCacheNames().contains("users")){
			cacheManager.getCache("users").put(user.getId(), user);
		}
	}

	@Cacheable(value = "users", key = "#userId")
	public User find(UUID userId) throws UserDoesNotExistsException {
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserDoesNotExistsException(userId.toString(), "User does not exists"));
	}

	@Cacheable(value = "users", key = "#container.user.id")
	public User find(AuthenticationContainer container) {
		return userRepository.findById(container.getUser().getId())
				.orElseThrow(() -> new IllegalStateException("User from authentication does not exists"));
	}

	private String getRemoteAddress(HttpServletRequest httpRequest) {
		if (httpRequest.getHeader("x-forwarded-for") != null) {
			return httpRequest.getHeader("x-forwarded-for").split(",")[0];
		}
		else return httpRequest.getRemoteUser();
	}
}
