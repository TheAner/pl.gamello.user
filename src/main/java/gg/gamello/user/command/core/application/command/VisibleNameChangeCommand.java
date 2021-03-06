package gg.gamello.user.command.core.application.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class VisibleNameChangeCommand {
	@NotEmpty(message = "VisibleName can not be empty")
	@Size(min = 3, max = 24, message = "VisibleName must be between {min} and {max} characters long")
	String visibleName;
}
