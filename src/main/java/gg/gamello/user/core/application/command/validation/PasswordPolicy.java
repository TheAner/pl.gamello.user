package gg.gamello.user.core.application.command.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Constraint(validatedBy={})
@Retention(RUNTIME)
@Pattern(regexp="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,64})")
public @interface PasswordPolicy {
	String message() default "Password must be 8 to 64 long, contains upper, lower case characters," +
			" one numeric and one special character";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
