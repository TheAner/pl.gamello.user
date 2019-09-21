package gg.gamello.user.controller.advisor;

import gg.gamello.user.exception.ErrorMessage;
import gg.gamello.user.exception.token.InvalidTypeOfTokenException;
import gg.gamello.user.exception.token.TokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class TokenExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(TokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorMessage tokenNotFoundHandler(TokenNotFoundException ex) {
        log.error(ex.getMessage());
        return ErrorMessage.builder()
                .error(ex.getMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {InvalidTypeOfTokenException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ErrorMessage invalidTypeOfTokenHandler(Exception ex) {
        log.error(ex.getMessage());
        return ErrorMessage.builder()
                .error(ex.getMessage())
                .build();
    }
}
