package gg.gamello.user.core.application;

import gg.gamello.user.core.application.command.*;
import gg.gamello.user.core.domain.User;
import gg.gamello.user.core.domain.UserFactory;
import gg.gamello.user.core.domain.UserRepository;
import gg.gamello.user.core.infrastructure.exception.PasswordsDontMatchException;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.core.infrastructure.exception.UserIsNotActiveException;
import gg.gamello.user.infrastructure.security.AuthenticationUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserAuthApplicationService {

	private UserFactory userFactory;

	private UserRepository userRepository;

	private final PasswordEncoder encoder;

	public UserAuthApplicationService(UserFactory userFactory, UserRepository userRepository, PasswordEncoder encoder) {
		this.userFactory = userFactory;
		this.userRepository = userRepository;
		this.encoder = encoder;
	}

	@Transactional //todo: Add token request
	public User create(RegisterCommand command) throws UserAlreadyExistsException {
		if (userRepository.existsUserByEmailOrUsername(command.getEmail(), command.getUsername()))
			throw new UserAlreadyExistsException("User with credentials "  +
					command.getEmail() + "/" + command.getUsername() + " already exists");

		User user = userFactory.create(command);
		return userRepository.save(user);
	}

	@Transactional //todo: Add token validation
	public void activate(ActivateCommand command) throws UserDoesNotExistsException {
		User user = findUser(command.getUserId());
		user.activate();
		userRepository.save(user);
	}

	@Transactional //todo: Add token validation
	public void delete(DeleteCommand command) throws UserDoesNotExistsException {
		User user = findUser(command.getUserId());
		userRepository.delete(user);
	}

	@Transactional //todo: Add token validation
	public void recover(RecoverCommand command) throws UserDoesNotExistsException, UserIsNotActiveException {
		User user = findUser(command.getUserId());
		user.checkActive();
		user.changePassword(command.getPassword(), encoder);
		userRepository.save(user);
	}

	@Transactional
	public void changePassword(AuthenticationUser authenticationUser, PasswordChangeCommand command) throws PasswordsDontMatchException {
		User user = findUser(authenticationUser);
		user.matchPassword(command.getOldPassword(), command.getNewPassword(), encoder);
		user.changePassword(command.getNewPassword(), encoder);
		userRepository.save(user);
	}

	@Transactional //todo: Add token validation
	public void changeEmail(EmailChangeCommand command) throws UserDoesNotExistsException {
		User user = findUser(command.getUserId());
		//todo: obtain email from confirmation and set it
		user.changeEmail(user.getEmail());
		userRepository.save(user);
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
