package gg.gamello.user.controller.advisor;

import gg.gamello.user.exception.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

@Slf4j
@ControllerAdvice
public class ServiceExceptionAdvice {

    @ResponseBody
    @ExceptionHandler({RestClientException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    ErrorMessage ServiceExceptionHandler(Exception ex) {
        log.error(ex.getMessage() + " - connection failed");

        return ErrorMessage.builder()
                .error("Service is currently unavailable")
                .build();
    }
}
