package gg.gamello.user.confirmation.infrastructure.advisor;

import gg.gamello.user.confirmation.infrastructure.exception.ConfirmationDoesNotExistsException;
import gg.gamello.user.confirmation.infrastructure.exception.IncorrectSecretException;
import gg.gamello.user.confirmation.infrastructure.exception.OutdatedConfirmationException;
import gg.gamello.user.infrastructure.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ConfirmationAdvisor {

	@ResponseBody
	@ExceptionHandler(ConfirmationDoesNotExistsException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ErrorMessage confirmationNotFoundHandler(ConfirmationDoesNotExistsException ex) {
		return ErrorMessage.builder()
				.error(ex.getMessage())
				.build();
	}

	@ResponseBody
	@ExceptionHandler(value = {IncorrectSecretException.class, OutdatedConfirmationException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	ErrorMessage unacceptableConfirmationHandler(Exception ex) {
		return ErrorMessage.builder()
				.error(ex.getMessage())
				.build();
	}
}
