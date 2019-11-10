package gg.gamello.user.core.ui;

import gg.gamello.user.core.application.UserApplicationService;
import gg.gamello.user.core.application.command.EmailChangeRequestCommand;
import gg.gamello.user.core.application.command.LanguageChangeCommand;
import gg.gamello.user.infrastructure.security.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	UserApplicationService applicationService;

	@GetMapping("/")
	public ResponseEntity<Response> getLoggedUser(@AuthenticationPrincipal AuthenticationUser user) {
		var userDto = applicationService.getLoggedUser(user);
		return ResponseEntity
				.ok(Response.builder()
						.status(HttpStatus.OK.getReasonPhrase())
						.code(HttpStatus.OK.value())
						.body(userDto)
						.build());
	}

	@PostMapping("/change/language")
	public ResponseEntity<Response> emailChangeRequest(@AuthenticationPrincipal AuthenticationUser user,
													   @RequestBody LanguageChangeCommand command) {
		applicationService.changeLanguage(user, command);
		return ResponseEntity
				.ok(Response.builder()
						.status(HttpStatus.OK.getReasonPhrase())
						.code(HttpStatus.OK.value())
						.build());
	}
}
