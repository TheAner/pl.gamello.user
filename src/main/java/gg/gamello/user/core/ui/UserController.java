package gg.gamello.user.core.ui;

import gg.gamello.dev.authentication.model.User;
import gg.gamello.user.core.application.UserApplicationService;
import gg.gamello.user.core.application.dto.UserDto;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController { //todo: delete

	@Autowired
	UserApplicationService userService;

	@GetMapping("/") //todo: move into cqrs in future
	public ResponseEntity<UserDto> getLogged(@AuthenticationPrincipal User user) {
		var userDto = userService.getLogged(AuthenticationContainer.contain(user));
		return ResponseEntity.ok(userDto);
	}

	@GetMapping("/special/{slug}") //todo: move into cqrs in future
	public ResponseEntity<UserDto> getBySlug(@PathVariable String slug) throws UserDoesNotExistsException {
		var userDto = userService.getBySlug(slug);
		return ResponseEntity.ok(userDto);
	}
}
