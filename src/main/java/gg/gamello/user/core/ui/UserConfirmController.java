package gg.gamello.user.core.ui;

import gg.gamello.dev.authentication.model.User;
import gg.gamello.user.confirmation.infrastructure.exception.ConfirmationDoesNotExistsException;
import gg.gamello.user.confirmation.infrastructure.exception.ConfirmationException;
import gg.gamello.user.confirmation.infrastructure.exception.IncorrectSecretException;
import gg.gamello.user.confirmation.infrastructure.exception.OutdatedConfirmationException;
import gg.gamello.user.core.application.UserConfirmApplicationService;
import gg.gamello.user.core.application.command.*;
import gg.gamello.user.core.application.dto.UserDto;
import gg.gamello.user.core.infrastructure.exception.PasswordsDontMatchException;
import gg.gamello.user.core.infrastructure.exception.PropertyConflictException;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.core.infrastructure.exception.UserIsNotActiveException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserConfirmController {

	@Autowired
	UserConfirmApplicationService applicationService;

	@PostMapping("/confirm")
	public ResponseEntity<Void> confirmUser(@Valid @RequestBody ConfirmCommand command)
			throws ConfirmationException, UserDoesNotExistsException {
		applicationService.activate(command);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/confirm")
	public ResponseEntity<Void> confirmUserDelete(@Valid @RequestBody ConfirmCommand command)
			throws ConfirmationException, UserDoesNotExistsException {
		applicationService.delete(command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/confirm/email")
	public ResponseEntity<Void> confirmEmail(@Valid @RequestBody ConfirmCommand command)
			throws ConfirmationException, UserDoesNotExistsException {
		applicationService.changeEmail(command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/confirm/password")
	public ResponseEntity<Void> confirmPassword(@Valid @RequestBody RecoverConfirmCommand command)
			throws ConfirmationException, UserDoesNotExistsException {
		applicationService.recover(command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/change/password")
	public ResponseEntity<Void> passwordChange(@AuthenticationPrincipal User user,
											   @Valid @RequestBody PasswordChangeCommand command)
			throws PasswordsDontMatchException, PropertyConflictException {
		applicationService.changePassword(AuthenticationContainer.contain(user), command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/check")
	public ResponseEntity<Void> checkSecret(@Valid @RequestBody CheckSecretCommand command)
			throws IncorrectSecretException, ConfirmationDoesNotExistsException, OutdatedConfirmationException {
		applicationService.checkSecret(command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/api/authenticate")
	public UserDto authenticateCredentials(@RequestBody CredentialsCommand command)
			throws UserDoesNotExistsException, UserIsNotActiveException {
		return applicationService.authenticateCredentials(command);
	}
}
