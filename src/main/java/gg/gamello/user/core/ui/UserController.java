package gg.gamello.user.core.ui;

import gg.gamello.user.core.application.UserApplicationService;
import gg.gamello.user.core.application.command.LanguageChangeCommand;
import gg.gamello.user.core.application.command.SlugChangeCommand;
import gg.gamello.user.core.application.command.VisibleNameChangeCommand;
import gg.gamello.user.core.application.dto.UserDto;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

	@Autowired
	UserApplicationService applicationService;

	@GetMapping("/")
	public ResponseEntity<UserDto> getLoggedUser(@AuthenticationPrincipal AuthenticationUser user) {
		var userDto = applicationService.getLoggedUser(user);
		return ResponseEntity.ok(userDto);
	}

	@GetMapping("/special/{slug}")
	public ResponseEntity<UserDto> getLoggedUser(@PathVariable String slug) throws UserDoesNotExistsException {
		var userDto = applicationService.getUserBySlug(slug);
		return ResponseEntity.ok(userDto);
	}

	@PostMapping("/change/language")
	public ResponseEntity<Void> languageChange(@AuthenticationPrincipal AuthenticationUser user,
													   @RequestBody LanguageChangeCommand command) {
		applicationService.changeLanguage(user, command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/change/slug")
	public ResponseEntity<Void> slugChange(@AuthenticationPrincipal AuthenticationUser user,
												   @RequestBody SlugChangeCommand command) throws UserAlreadyExistsException {
		applicationService.changeSlug(user, command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/change/name")
	public ResponseEntity<Void> visibleNameChange(@AuthenticationPrincipal AuthenticationUser user,
										   @RequestBody VisibleNameChangeCommand command) {
		applicationService.changeVisibleName(user, command);
		return ResponseEntity.ok().build();
	}
}
