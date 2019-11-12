package gg.gamello.user.core.application.command;

import gg.gamello.user.core.application.command.validation.PasswordPolicy;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RecoverConfirmCommand extends ConfirmCommand {
	@NotEmpty(message = "Password can not be empty")
	@PasswordPolicy
	String password;
}
