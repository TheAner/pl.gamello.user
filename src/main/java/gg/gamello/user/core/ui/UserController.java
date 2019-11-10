package gg.gamello.user.core.ui;

import gg.gamello.user.core.application.UserApplicationService;
import gg.gamello.user.core.application.command.LanguageChangeCommand;
import gg.gamello.user.core.application.command.SlugChangeCommand;
import gg.gamello.user.core.application.dto.UserDto;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	UserApplicationService applicationService;

	@GetMapping("/")
	public ResponseEntity<UserDto> getLoggedUser(@AuthenticationPrincipal AuthenticationUser user) {
		var userDto = applicationService.getLoggedUser(user);
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
}
