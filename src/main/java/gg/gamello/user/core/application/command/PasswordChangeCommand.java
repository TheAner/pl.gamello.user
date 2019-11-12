package gg.gamello.user.core.application.command;

import gg.gamello.user.core.application.command.validation.PasswordPolicy;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PasswordChangeCommand {
	@NotEmpty(message = "Old password can not be empty")
	@Size(min = 8, max = 64, message = "Password must be between {min} and {max} characters long")
	String oldPassword;

	@NotEmpty(message = "New password can not be empty")
	@Size(min = 8, max = 64, message = "Password must be between {min} and {max} characters long")
	@PasswordPolicy()
	String newPassword;
}
