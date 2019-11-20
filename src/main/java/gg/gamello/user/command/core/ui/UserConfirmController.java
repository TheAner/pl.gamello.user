package gg.gamello.user.command.core.ui;

import gg.gamello.user.command.confirmation.infrastructure.exception.ConfirmationException;
import gg.gamello.user.command.core.application.UserConfirmApplicationService;
import gg.gamello.user.command.core.application.command.ConfirmCommand;
import gg.gamello.user.command.core.application.command.RecoverConfirmCommand;
import gg.gamello.user.command.core.infrastructure.exception.UserDoesNotExistsException;
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
}
