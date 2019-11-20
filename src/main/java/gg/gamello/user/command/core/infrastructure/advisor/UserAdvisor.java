package gg.gamello.user.command.core.infrastructure.advisor;

import gg.gamello.user.command.core.infrastructure.exception.PasswordsDontMatchException;
import gg.gamello.user.command.core.infrastructure.exception.PropertyConflictException;
import gg.gamello.user.command.core.infrastructure.exception.UserDoesNotExistsException;
import gg.gamello.user.command.core.infrastructure.exception.UserIsNotActiveException;
import gg.gamello.user.infrastructure.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserAdvisor {

	@ResponseBody
	@ExceptionHandler(UserDoesNotExistsException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ErrorMessage accountNotFoundHandler(UserDoesNotExistsException ex) {
		return ErrorMessage.builder()
				.error(ex.getMessage())
				.build();
	}

	@ResponseBody
	@ExceptionHandler(PropertyConflictException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	ErrorMessage accountAlreadyExistsHandler(PropertyConflictException ex) {
		return ErrorMessage.builder()
				.error("Property conflict has occurred")
				.addDetail(ex.getPropertyName(), ex.getMessage())
				.build();
	}

	@ResponseBody
	@ExceptionHandler(UserIsNotActiveException.class)
	@ResponseStatus(HttpStatus.LOCKED)
	ErrorMessage accountIsNotActiveHandler(UserIsNotActiveException ex) {
		return ErrorMessage.builder()
				.error(ex.getMessage())
				.build();
	}

	@ResponseBody
	@ExceptionHandler(PasswordsDontMatchException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	ErrorMessage passwordsDontMatchHandler(PasswordsDontMatchException ex) {
		return ErrorMessage.builder()
				.error(ex.getMessage())
				.build();
	}
}
