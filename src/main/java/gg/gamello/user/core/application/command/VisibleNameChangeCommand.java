package gg.gamello.user.core.application.command;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class VisibleNameChangeCommand {
	@NotEmpty(message = "VisibleName can not be empty")
	@Length(min = 3, max = 24, message = "VisibleName must be between {min} and {max} characters long")
	String visibleName;
}
