package gg.gamello.user.core.application.command;

import gg.gamello.user.core.application.command.validation.PasswordPolicy;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PasswordChangeCommand {
	@NotEmpty(message = "Old password can not be empty")
	String oldPassword;

	@NotEmpty(message = "New password can not be empty")
	@PasswordPolicy
	String newPassword;
}
