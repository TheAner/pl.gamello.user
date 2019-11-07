package gg.gamello.user.core.application;

import gg.gamello.user.core.application.command.ActivateCommand;
import gg.gamello.user.core.application.command.RegisterCommand;
import gg.gamello.user.core.domain.User;
import gg.gamello.user.core.domain.UserFactory;
import gg.gamello.user.core.domain.UserRepository;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserApplicationService {

	private UserFactory userFactory;

	private UserRepository userRepository;

	public UserApplicationService(UserFactory userFactory, UserRepository userRepository) {
		this.userFactory = userFactory;
		this.userRepository = userRepository;
	}

	@Transactional
	public User create(RegisterCommand command) throws UserAlreadyExistsException {
		if (userRepository.existsUserByEmailOrUsername(command.getEmail(), command.getUsername()))
			throw new UserAlreadyExistsException("User with credentials "  +
					command.getEmail() + "/" + command.getUsername() + " already exists");

		User user = userFactory.create(command);
		return userRepository.save(user);
	}

	@Transactional
	public void activate(ActivateCommand command) throws UserDoesNotExistsException {
		User user = findUser(command.getUserId());
		user.activate();
		userRepository.save(user);
	}

	private User findUser(UUID userId) throws UserDoesNotExistsException {
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserDoesNotExistsException(userId.toString(), "user does not exists"));
	}
}
