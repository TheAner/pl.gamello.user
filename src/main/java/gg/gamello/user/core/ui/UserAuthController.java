package gg.gamello.user.core.ui;

import gg.gamello.user.core.application.UserRequestApplicationService;
import gg.gamello.user.core.application.command.EmailChangeRequestCommand;
import gg.gamello.user.core.application.command.RecoverRequestCommand;
import gg.gamello.user.core.application.command.RegisterCommand;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class UserAuthController {

	@Autowired
	UserRequestApplicationService applicationService;

	@PostMapping("/")
	public ResponseEntity<Void> register(@RequestBody RegisterCommand command) throws UserAlreadyExistsException {
		var userId = applicationService.create(command);
		return ResponseEntity.created(URI.create("user/" + userId)).build();
	}

	@DeleteMapping("/")
	public ResponseEntity<Void> deleteRequest(@AuthenticationPrincipal AuthenticationUser user) {
		applicationService.createDeleteRequest(user);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/change/email")
	public ResponseEntity<Void> emailChangeRequest(@AuthenticationPrincipal AuthenticationUser user,
													   @RequestBody EmailChangeRequestCommand command) {
		applicationService.createEmailChangeRequest(user, command);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/recover")
	public ResponseEntity<Void> recoverRequest(@RequestBody RecoverRequestCommand command) {
		applicationService.createRecoverRequest(command);
		return ResponseEntity.ok().build();
	}
}
