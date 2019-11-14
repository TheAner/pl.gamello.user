package gg.gamello.user.core.ui;

import gg.gamello.dev.authentication.model.User;
import gg.gamello.user.core.application.UserRequestApplicationService;
import gg.gamello.user.core.application.command.EmailChangeRequestCommand;
import gg.gamello.user.core.application.command.RecoverRequestCommand;
import gg.gamello.user.core.application.command.RegisterCommand;
import gg.gamello.user.core.infrastructure.exception.PropertyConflictException;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class UserAuthController {

	@Autowired
	UserRequestApplicationService applicationService;

	@PostMapping("/")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterCommand command)
			throws UserAlreadyExistsException {
		var userId = applicationService.create(command);
		return ResponseEntity.created(URI.create("user/" + userId)).build();
	}

	@DeleteMapping("/")
	public ResponseEntity<Void> deleteRequest(@AuthenticationPrincipal User user) {
		applicationService.createDeleteRequest(AuthenticationContainer.contain(user));
		return ResponseEntity.ok().build();
	}

	@PostMapping("/change/email")
	public ResponseEntity<Void> emailChangeRequest(@AuthenticationPrincipal User user,
												   @Valid @RequestBody EmailChangeRequestCommand command)
			throws PropertyConflictException {
		applicationService.createEmailChangeRequest(AuthenticationContainer.contain(user), command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/recover")
	public ResponseEntity<Void> recoverRequest(@Valid @RequestBody RecoverRequestCommand command) {
		applicationService.createRecoverRequest(command);
		return ResponseEntity.ok().build();
	}
}
