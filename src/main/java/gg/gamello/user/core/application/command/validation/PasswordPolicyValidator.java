package gg.gamello.user.core.application.command.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordPolicyValidator implements ConstraintValidator<PasswordPolicy, String> {

	private String message;
	private String regexp;

	@Override
	public void initialize(PasswordPolicy constraintAnnotation) {
		message = constraintAnnotation.message();
		regexp = constraintAnnotation.regexp();
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (password != null && password.matches(regexp))
			return true;
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
		return false;
	}
}
