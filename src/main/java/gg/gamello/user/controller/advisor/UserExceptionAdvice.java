package gg.gamello.user.controller.advisor;

import gg.gamello.user.exception.ErrorMessage;
import gg.gamello.user.exception.PasswordsDontMatchException;
import gg.gamello.user.exception.user.UserAlreadyExistsException;
import gg.gamello.user.exception.user.UserDoesNotExistsException;
import gg.gamello.user.exception.user.UserIsNotActiveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class UserExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(UserDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorMessage accountNotFoundHandler(UserDoesNotExistsException ex) {
        log.error(ex.getMessage());
        return ErrorMessage.builder()
                .error(ex.getMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorMessage accountAlreadyExistsHandler(UserAlreadyExistsException ex) {
        log.error(ex.getMessage());
        return ErrorMessage.builder()
                .error(ex.getMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(UserIsNotActiveException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    ErrorMessage accountIsNotActiveHandler(UserIsNotActiveException ex) {
        log.error(ex.getMessage());
        return ErrorMessage.builder()
                .error(ex.getMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(PasswordsDontMatchException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ErrorMessage passwordsDontMatchHandler(PasswordsDontMatchException ex) {
        log.error(ex.getMessage());
        return ErrorMessage.builder()
                .error(ex.getMessage())
                .build();
    }
}
