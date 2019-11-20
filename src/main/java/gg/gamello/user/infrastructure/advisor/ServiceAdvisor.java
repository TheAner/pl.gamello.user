package gg.gamello.user.infrastructure.advisor;

import com.fasterxml.jackson.databind.JsonMappingException;
import gg.gamello.user.command.avatar.exception.InvalidFileException;
import gg.gamello.user.infrastructure.exception.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
public class ServiceAdvisor {

	@ResponseBody
	@ExceptionHandler({RestClientException.class, IllegalStateException.class, InterruptedException.class})
	@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
	ErrorMessage serviceExceptionHandler(Exception ex) {
		log.error(ex.getMessage() + " - connection failed");
		return ErrorMessage.builder()
				.error("Service is currently unavailable")
				.build();
	}

	@ResponseBody
	@ExceptionHandler(InvalidFileException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	ErrorMessage invalidFIleHandler(Exception ex) {
		return ErrorMessage.builder()
				.error(ex.getMessage())
				.build();
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	ErrorMessage onConstraintValidationException(ConstraintViolationException e) {
		var error = ErrorMessage.builder()
				.error("Validation failed for constraint");
		for (ConstraintViolation violation : e.getConstraintViolations()) {
			error.addDetail(violation.getPropertyPath().toString(), violation.getMessage());
		}
		return error.build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	ErrorMessage onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		var error = ErrorMessage.builder()
				.error("Validation failed for argument [" + e.getBindingResult().getObjectName() + "]");
		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			error.addDetail(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return error.build();
	}

	@ExceptionHandler(JsonMappingException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	ErrorMessage onJsonMappingException(JsonMappingException e) {
		var error = ErrorMessage.builder()
				.error("Mapping failed for argument [" + e.getPath().get(0).getFieldName() + "]");
		var referenceChain = e.getMessage().indexOf("through reference chain");
		error.addDetail(e.getPath().get(0).getFieldName(),
				e.getMessage().substring(0, (referenceChain>0?referenceChain-1:e.getMessage().length()-1)));
		log.debug(error.build().toString());
		return ErrorMessage.builder().error("Input must be valid").build();
	}
}
