package gg.gamello.user.core.ui;

import gg.gamello.dev.authentication.model.User;
import gg.gamello.user.avatar.exception.AvatarException;
import gg.gamello.user.core.application.UserApplicationService;
import gg.gamello.user.core.application.command.LanguageChangeCommand;
import gg.gamello.user.core.application.command.SlugChangeCommand;
import gg.gamello.user.core.application.command.VisibleNameChangeCommand;
import gg.gamello.user.core.application.dto.UserDto;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
public class UserController {

	@Autowired
	UserApplicationService applicationService;

	@GetMapping("/")
	public ResponseEntity<UserDto> getLoggedUser(@AuthenticationPrincipal AuthenticationContainer user) {
		var userDto = applicationService.getLoggedUser(user);
		return ResponseEntity.ok(userDto);
	}

	@GetMapping("/special/{slug}")
	public ResponseEntity<UserDto> getLoggedUser(@PathVariable String slug) throws UserDoesNotExistsException {
		var userDto = applicationService.getUserBySlug(slug);
		return ResponseEntity.ok(userDto);
	}

	@PostMapping("/change/language")
	public ResponseEntity<Void> languageChange(@AuthenticationPrincipal User user,
													   @RequestBody LanguageChangeCommand command) {
		applicationService.changeLanguage(AuthenticationContainer.contain(user), command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/change/slug")
	public ResponseEntity<Void> slugChange(@AuthenticationPrincipal User user,
										   @Valid @RequestBody SlugChangeCommand command)
			throws UserAlreadyExistsException {
		applicationService.changeSlug(AuthenticationContainer.contain(user), command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/change/name")
	public ResponseEntity<Void> visibleNameChange(@AuthenticationPrincipal User user,
												  @Valid @RequestBody VisibleNameChangeCommand command) {
		applicationService.changeVisibleName(AuthenticationContainer.contain(user), command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/change/avatar")
	public ResponseEntity<Void> avatarChange(@AuthenticationPrincipal User user,
											 @RequestParam("file") MultipartFile image)
			throws AvatarException, InterruptedException {
		applicationService.changeAvatar(AuthenticationContainer.contain(user), image);
		return ResponseEntity.ok().build();
	}
}
