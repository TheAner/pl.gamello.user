package gg.gamello.user.core.ui;

import gg.gamello.user.core.application.UserRequestApplicationService;
import gg.gamello.user.core.application.command.EmailChangeRequestCommand;
import gg.gamello.user.core.application.command.RecoverRequestCommand;
import gg.gamello.user.core.application.command.RegisterCommand;
import gg.gamello.user.core.infrastructure.exception.UserAlreadyExistsException;
import gg.gamello.user.infrastructure.security.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<Response> register(@RequestBody RegisterCommand command) throws UserAlreadyExistsException {
		var userId = applicationService.create(command);
		return ResponseEntity.created(URI.create("user/" + userId))
				.body(Response.builder()
						.status(HttpStatus.CREATED.getReasonPhrase())
						.code(HttpStatus.CREATED.value())
						.body("user/" + userId)
						.build());
	}

	@DeleteMapping("/")
	public ResponseEntity<Response> deleteRequest(@AuthenticationPrincipal AuthenticationUser user) {
		applicationService.createDeleteRequest(user);
		return ResponseEntity
				.ok(Response.builder()
						.status(HttpStatus.OK.getReasonPhrase())
						.code(HttpStatus.OK.value())
						.build());
	}

	@PostMapping("/change/email")
	public ResponseEntity<Response> emailChangeRequest(@AuthenticationPrincipal AuthenticationUser user,
													   @RequestBody EmailChangeRequestCommand command) {
		applicationService.createEmailChangeRequest(user, command);
		return ResponseEntity
				.ok(Response.builder()
						.status(HttpStatus.OK.getReasonPhrase())
						.code(HttpStatus.OK.value())
						.build());
	}

	@PostMapping("/recover")
	public ResponseEntity<Response> recoverRequest(@RequestBody RecoverRequestCommand command) {
		applicationService.createRecoverRequest(command);
		return ResponseEntity
				.ok(Response.builder()
						.status(HttpStatus.OK.getReasonPhrase())
						.code(HttpStatus.OK.value())
						.build());
	}
}
