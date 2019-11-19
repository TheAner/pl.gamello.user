package gg.gamello.user.core.ui;

import gg.gamello.user.confirmation.infrastructure.exception.ConfirmationDoesNotExistsException;
import gg.gamello.user.confirmation.infrastructure.exception.ConfirmationException;
import gg.gamello.user.confirmation.infrastructure.exception.IncorrectSecretException;
import gg.gamello.user.confirmation.infrastructure.exception.OutdatedConfirmationException;
import gg.gamello.user.core.application.UserConfirmApplicationService;
import gg.gamello.user.core.application.command.CheckSecretCommand;
import gg.gamello.user.core.application.command.ConfirmCommand;
import gg.gamello.user.core.application.command.CredentialsCommand;
import gg.gamello.user.core.application.command.RecoverConfirmCommand;
import gg.gamello.user.core.application.dto.UserDto;
import gg.gamello.user.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.core.infrastructure.exception.UserIsNotActiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserConfirmController {

	@Autowired
	UserConfirmApplicationService confirmService;

	@PostMapping("/confirm")
	public void activate(@Valid @RequestBody ConfirmCommand command)
			throws ConfirmationException, UserDoesNotExistsException {
		confirmService.activate(command);
	}

	@DeleteMapping("/confirm")
	public void delete(@Valid @RequestBody ConfirmCommand command)
			throws ConfirmationException, UserDoesNotExistsException {
		confirmService.delete(command);
	}

	@PostMapping("/confirm/email")
	public void email(@Valid @RequestBody ConfirmCommand command)
			throws ConfirmationException, UserDoesNotExistsException {
		confirmService.changeEmail(command);
	}

	@PostMapping("/confirm/password")
	public void password(@Valid @RequestBody RecoverConfirmCommand command)
			throws ConfirmationException, UserDoesNotExistsException {
		confirmService.recover(command);
	}

	@PostMapping("/check")
	public void checkSecret(@Valid @RequestBody CheckSecretCommand command)
			throws IncorrectSecretException, ConfirmationDoesNotExistsException, OutdatedConfirmationException {
		confirmService.checkSecret(command);
	}

	@PostMapping("/api/authenticate")
	public UserDto authenticateCredentials(@RequestBody CredentialsCommand command)
			throws UserDoesNotExistsException, UserIsNotActiveException {
		return confirmService.authenticateCredentials(command);
	}
}
