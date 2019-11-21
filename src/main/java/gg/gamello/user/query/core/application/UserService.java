package gg.gamello.user.query.core.application;

import gg.gamello.user.command.confirmation.aplication.command.ConfirmationCommand;
import gg.gamello.user.command.confirmation.domain.action.ActionType;
import gg.gamello.user.command.confirmation.infrastructure.exception.ConfirmationDoesNotExistsException;
import gg.gamello.user.command.confirmation.infrastructure.exception.IncorrectSecretException;
import gg.gamello.user.command.confirmation.infrastructure.exception.OutdatedConfirmationException;
import gg.gamello.user.command.core.domain.User;
import gg.gamello.user.command.core.domain.UserRepository;
import gg.gamello.user.command.core.domain.confirmation.Confirmation;
import gg.gamello.user.command.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.command.core.infrastructure.exception.UserIsNotActiveException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import gg.gamello.user.query.core.application.dto.UserDto;
import gg.gamello.user.query.core.application.dto.UserDtoAssembler;
import gg.gamello.user.query.core.application.query.CheckSecretQuery;
import gg.gamello.user.query.core.application.query.CredentialsQuery;
import gg.gamello.user.query.core.application.query.UsersQuery;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder encoder;
	private final Confirmation confirmation;

	@Resource
	private UserService self;

	public UserService(UserRepository userRepository, PasswordEncoder encoder, Confirmation confirmation) {
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.confirmation = confirmation;
	}

	public UserDto getLogged(AuthenticationContainer container) {
		User user = Optional.ofNullable(self.find(container.getUser().getId()))
				.orElseThrow(() -> new IllegalStateException("User from authentication does not exists"));

		return UserDtoAssembler.builder(user).detailed().withLanguage().build();
	}

	public UserDto getBySlug(String slug) throws UserDoesNotExistsException {
		return UserDtoAssembler.builder(self.find(slug)).detailed().build();
	}

	public UserDto getById(UUID userId) throws UserDoesNotExistsException {
		User user = Optional.ofNullable(self.find(userId))
				.orElseThrow(() -> new UserDoesNotExistsException(userId.toString(), "User does not exists"));

		return UserDtoAssembler.builder(user).detailed().build();
	}

	public UserDto getSecuredById(UUID userId) throws UserDoesNotExistsException {
		User user = Optional.ofNullable(self.find(userId))
				.orElseThrow(() -> new UserDoesNotExistsException(userId.toString(), "User does not exists"));

		return UserDtoAssembler.builder(user).secured().build();
	}

	public Set<UserDto> getById(UsersQuery query) {
		return getById(query, false);
	}

	public Set<UserDto> getById(UsersQuery query, boolean detailed) {
		if (query.getUserIds() == null || query.getUserIds().isEmpty())
			return Set.of();
		Set<UserDto> users = new HashSet<>(query.getUserIds().size());
		Set<User> usersFromRepository = query.getUserIds().stream()
				.map(uuid -> self.find(uuid))
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());

		if (detailed) {
			usersFromRepository.forEach(user -> users
					.add(UserDtoAssembler.builder(user).detailed().build()));
		} else {
			usersFromRepository.forEach(user -> users
					.add(UserDtoAssembler.builder(user).simple().build()));
		}

		return users;
	}

	public void checkSecret(CheckSecretQuery command)
			throws IncorrectSecretException, ConfirmationDoesNotExistsException, OutdatedConfirmationException {
		var confirmationCommand = ConfirmationCommand.builder()
				.userId(command.getUserId())
				.secret(command.getSecret())
				.action(ActionType.valueOf(command.getAction()))
				.build();

		confirmation.check(confirmationCommand);
	}

	public UserDto authenticateCredentials(CredentialsQuery command) throws UserDoesNotExistsException, UserIsNotActiveException {
		User user = userRepository.findByCredentials(command.getLogin())
				.orElseThrow(() -> new UserDoesNotExistsException(command.getLogin(), "User does not exists"));

		user.checkActive();

		if (!encoder.matches(command.getPassword(), user.getPassword()))
			throw new UserDoesNotExistsException(command.getLogin(), "User does not exists");

		return UserDtoAssembler.builder(user).secured().build();
	}

	@Cacheable(value = "users", key = "#userId")
	public User find(UUID userId) {
		return userRepository.findById(userId).orElse(null);
	}

	@Cacheable(value = "users", key = "#slug")
	public User find(String slug) throws UserDoesNotExistsException {
		return userRepository.findBySlug(slug)
				.orElseThrow(() -> new UserDoesNotExistsException(slug, "User does not exists"));
	}
}
