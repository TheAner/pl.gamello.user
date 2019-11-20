package gg.gamello.user.infrastructure.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<Enum, String> {

	private Set<String> values;

	@Override
	public void initialize(Enum constraintAnnotation) {
		if (constraintAnnotation.clazz().isEnum())
			values = Arrays.stream(constraintAnnotation.clazz().getEnumConstants())
					.map(java.lang.Enum::name)
					.collect(Collectors.toSet());
		if (constraintAnnotation.value().length>0)
			values = Set.of(constraintAnnotation.value());
	}

	@Override
	public boolean isValid(String enumValue, ConstraintValidatorContext context) {
		return values.contains(enumValue);
	}
}
