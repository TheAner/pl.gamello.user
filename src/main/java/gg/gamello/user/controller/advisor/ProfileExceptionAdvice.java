package gg.gamello.user.controller.advisor;

import gg.gamello.user.exception.ProfileDoesNotExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ProfileExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(ProfileDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String profileNotFoundHandler(ProfileDoesNotExistsException ex) {
        log.error(ex.getMessage());
        return ex.getMessage();
    }
}
