package gg.gamello.user.core.ui;

import gg.gamello.user.confirmation.infrastructure.exception.ConfirmationException;
import gg.gamello.user.core.application.UserConfirmApplicationService;
import gg.gamello.user.core.application.command.ConfirmCommand;
import gg.gamello.user.core.application.command.PasswordChangeCommand;
import gg.gamello.user.core.application.command.RecoverConfirmCommand;
import gg.gamello.user.core.infrastructure.exception.PasswordsDontMatchException;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserConfirmController {

	@Autowired
	UserConfirmApplicationService applicationService;

	@PostMapping("/confirm")
	public ResponseEntity<Void> confirmUser(@RequestBody ConfirmCommand command) throws ConfirmationException, UserDoesNotExistsException {
		applicationService.activate(command);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/confirm")
	public ResponseEntity<Void> confirmUserDelete(@RequestBody ConfirmCommand command) throws ConfirmationException, UserDoesNotExistsException {
		applicationService.activate(command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/confirm/email")
	public ResponseEntity<Void> confirmEmail(@RequestBody ConfirmCommand command) throws ConfirmationException, UserDoesNotExistsException {
		applicationService.changeEmail(command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/confirm/password")
	public ResponseEntity<Void> confirmPassword(@RequestBody RecoverConfirmCommand command) throws ConfirmationException, UserDoesNotExistsException {
		applicationService.recover(command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/change/password")
	public ResponseEntity<Void> passwordChange(@AuthenticationPrincipal AuthenticationUser user,
												   @RequestBody PasswordChangeCommand command) throws PasswordsDontMatchException {
		applicationService.changePassword(user, command);
		return ResponseEntity.ok().build();
	}
}
