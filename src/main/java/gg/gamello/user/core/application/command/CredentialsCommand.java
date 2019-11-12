package gg.gamello.user.core.application.command;

import gg.gamello.user.core.application.command.validation.PasswordPolicy;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CredentialsCommand {
	@NotEmpty(message = "Login can not be empty")
	private String login;

	@NotEmpty(message = "Password can not be empty")
	@PasswordPolicy
	private String password;
}
