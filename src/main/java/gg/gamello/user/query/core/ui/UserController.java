package gg.gamello.user.query.core.ui;

import gg.gamello.dev.authentication.model.User;
import gg.gamello.user.command.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.command.core.infrastructure.exception.UserIsNotActiveException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import gg.gamello.user.query.core.application.UserService;
import gg.gamello.user.query.core.application.dto.UserDto;
import gg.gamello.user.query.core.application.query.CredentialsQuery;
import gg.gamello.user.query.core.application.query.UsersQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public ResponseEntity<UserDto> getLogged(@AuthenticationPrincipal User user) {
		var userDto = userService.getLogged(AuthenticationContainer.contain(user));
		return ResponseEntity.ok(userDto);
	}

	@GetMapping(value = {"/{userId}", "/api/{userId}"})
	public ResponseEntity<UserDto> getById(@PathVariable UUID userId) throws UserDoesNotExistsException {
		var userDto = userService.getById(userId);
		return ResponseEntity.ok(userDto);
	}

	@GetMapping(value = "/", params = {"ids"})
	public ResponseEntity<Set<UserDto>> getByIds(@RequestParam("ids") List<UUID> ids) {
		var userDto = userService.getById(UsersQuery.from(ids));
		return ResponseEntity.ok(userDto);
	}

	@GetMapping(value = "/api/", params = {"ids"})
	public ResponseEntity<Set<UserDto>> getByIdsDetailed(@RequestParam("ids") List<UUID> ids) {
		var userDto = userService.getById(UsersQuery.from(ids), true);
		return ResponseEntity.ok(userDto);
	}

	@GetMapping("/special/{slug}")
	public ResponseEntity<UserDto> getBySlug(@PathVariable String slug) throws UserDoesNotExistsException {
		var userDto = userService.getBySlug(slug);
		return ResponseEntity.ok(userDto);
	}

	@PostMapping("/api/authenticate")
	public UserDto authenticateCredentials(@RequestBody CredentialsQuery command)
			throws UserDoesNotExistsException, UserIsNotActiveException {
		return userService.authenticateCredentials(command);
	}
}
