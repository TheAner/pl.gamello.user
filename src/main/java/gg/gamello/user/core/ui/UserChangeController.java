package gg.gamello.user.core.ui;

import gg.gamello.dev.authentication.model.User;
import gg.gamello.user.avatar.exception.AvatarException;
import gg.gamello.user.core.application.UserApplicationService;
import gg.gamello.user.core.application.UserConfirmApplicationService;
import gg.gamello.user.core.application.UserRequestApplicationService;
import gg.gamello.user.core.application.command.*;
import gg.gamello.user.core.infrastructure.exception.PasswordsDontMatchException;
import gg.gamello.user.core.infrastructure.exception.PropertyConflictException;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class UserChangeController {

	@Autowired
	UserRequestApplicationService requestService;
	@Autowired
	UserConfirmApplicationService confirmService;
	@Autowired
	UserApplicationService userService;

	//Need confirmation

	@PostMapping("/")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterCommand command)
			throws UserAlreadyExistsException {
		var userId = requestService.create(command);
		return ResponseEntity.created(URI.create("user/" + userId)).build(); //todo: change to correct path in future
	}

	@DeleteMapping("/")
	public void delete(@AuthenticationPrincipal User user) {
		requestService.createDeleteRequest(AuthenticationContainer.contain(user));
	}

	@PostMapping("/change/email")
	public void emailChange(@AuthenticationPrincipal User user,
											@Valid @RequestBody EmailChangeRequestCommand command)
			throws PropertyConflictException {
		requestService.createEmailChangeRequest(AuthenticationContainer.contain(user), command);
	}

	@PostMapping("/recover")
	public void recover(@Valid @RequestBody RecoverRequestCommand command) {
		requestService.createRecoverRequest(command);
	}

	//Don't need confirmation

	@PostMapping("/change/password")
	public void passwordChange(@AuthenticationPrincipal User user,
											   @Valid @RequestBody PasswordChangeCommand command)
			throws PasswordsDontMatchException, PropertyConflictException {
		confirmService.changePassword(AuthenticationContainer.contain(user), command);
	}

	@PostMapping("/change/language")
	public void languageChange(@AuthenticationPrincipal User user,
											   @RequestBody LanguageChangeCommand command) {
		userService.changeLanguage(AuthenticationContainer.contain(user), command);
	}

	@PostMapping("/change/slug")
	public void slugChange(@AuthenticationPrincipal User user,
										   @Valid @RequestBody SlugChangeCommand command)
			throws PropertyConflictException {
		userService.changeSlug(AuthenticationContainer.contain(user), command);
	}

	@PostMapping("/change/name")
	public void visibleNameChange(@AuthenticationPrincipal User user,
												  @Valid @RequestBody VisibleNameChangeCommand command)
			throws PropertyConflictException {
		userService.changeVisibleName(AuthenticationContainer.contain(user), command);
	}

	@PostMapping("/change/avatar")
	public void avatarChange(@AuthenticationPrincipal User user,
											 @RequestParam("file") MultipartFile image)
			throws AvatarException, InterruptedException {
		userService.changeAvatar(AuthenticationContainer.contain(user), image);
	}
}
