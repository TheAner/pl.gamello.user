package gg.gamello.user.controller.advisor;

import gg.gamello.user.exception.PasswordsDontMatchException;
import gg.gamello.user.exception.UserAlreadyExistsException;
import gg.gamello.user.exception.UserDoesNotExistsException;
import gg.gamello.user.exception.UserIsNotActiveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class AccountExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(UserDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String accountNotFoundHandler(UserDoesNotExistsException ex) {
        log.error(ex.getMessage());
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String accountAlreadyExistsHandler(UserAlreadyExistsException ex) {
        log.error(ex.getMessage());
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserIsNotActiveException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    String accountIsNotActiveHandler(UserIsNotActiveException ex) {
        log.error(ex.getMessage());
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(PasswordsDontMatchException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String passwordsDontMatchHandler(PasswordsDontMatchException ex) {
        log.error(ex.getMessage());
        return ex.getMessage();
    }
}
