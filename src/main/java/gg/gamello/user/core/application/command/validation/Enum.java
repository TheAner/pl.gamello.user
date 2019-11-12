package gg.gamello.user.core.application.command.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Constraint(validatedBy = EnumValidator.class)
@Retention(RUNTIME)
public @interface Enum {
	String message() default "Enum should be correctly";
	Class<? extends java.lang.Enum> clazz();
	String[] value() default {};
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
