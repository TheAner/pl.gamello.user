package gg.gamello.user.core.application.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RecoverConfirmCommand extends ConfirmCommand {
	@NotEmpty(message = "Password can not be empty")
	@Size(min = 8, max = 64, message = "Password must be between {min} and {max} characters long")
	String password;
}
