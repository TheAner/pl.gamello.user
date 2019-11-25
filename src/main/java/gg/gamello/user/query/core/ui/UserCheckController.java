package gg.gamello.user.query.core.ui;

import gg.gamello.user.command.confirmation.infrastructure.exception.ConfirmationDoesNotExistsException;
import gg.gamello.user.command.confirmation.infrastructure.exception.IncorrectSecretException;
import gg.gamello.user.command.confirmation.infrastructure.exception.OutdatedConfirmationException;
import gg.gamello.user.query.core.application.UserService;
import gg.gamello.user.query.core.application.query.CheckSecretQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserCheckController {
	@Autowired
	private UserService userService;

	@PostMapping("/check/secret")
	public void checkSecret(@Valid @RequestBody CheckSecretQuery command)
			throws IncorrectSecretException, ConfirmationDoesNotExistsException, OutdatedConfirmationException {
		userService.checkSecret(command);
	}
}
