package gg.gamello.user.query.core.ui;

import gg.gamello.user.command.confirmation.infrastructure.exception.ConfirmationDoesNotExistsException;
import gg.gamello.user.command.confirmation.infrastructure.exception.IncorrectSecretException;
import gg.gamello.user.command.confirmation.infrastructure.exception.OutdatedConfirmationException;
import gg.gamello.user.command.core.infrastructure.exception.PropertyConflictException;
import gg.gamello.user.query.core.application.UserService;
import gg.gamello.user.query.core.application.query.SecretCheckQuery;
import gg.gamello.user.query.core.application.query.SlugCheckQuery;
import gg.gamello.user.query.core.application.query.UsernameCheckQuery;
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
	public void checkSecret(@Valid @RequestBody SecretCheckQuery query)
			throws IncorrectSecretException, ConfirmationDoesNotExistsException, OutdatedConfirmationException {
		userService.checkSecret(query);
	}

	@PostMapping("/check/username")
	public void checkUsername(@Valid @RequestBody UsernameCheckQuery query) throws PropertyConflictException {
		userService.checkUsername(query);
	}

	@PostMapping("/check/slug")
	public void checkSlug(@Valid @RequestBody SlugCheckQuery query) throws PropertyConflictException {
		userService.checkSlug(query);
	}
}
