package gg.gamello.user.core.application.command.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Constraint(validatedBy = PasswordPolicyValidator.class)
@Retention(RUNTIME)
public @interface PasswordPolicy {
	String message() default "Password must be 8 to 64 long, contains upper, lower case characters," +
			" one numeric and one special character";
	String regexp() default "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,64})";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
