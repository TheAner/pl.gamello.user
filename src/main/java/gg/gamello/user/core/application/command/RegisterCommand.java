package gg.gamello.user.core.application.command;

import gg.gamello.user.core.application.command.validation.PasswordPolicy;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegisterCommand {
	@NotEmpty(message = "Username can not be empty")
	@Size(min = 3, max = 24, message = "Username must be between {min} and {max} characters long")
	String username;

	@NotEmpty(message = "Email can not be empty")
	@Email(message = "Email should be correctly")
	String email;

	@NotEmpty(message = "Password can not be empty")
	@PasswordPolicy
	String password;

	@NotEmpty(message = "Language can not be empty")
	String language;

	public void setUsername(String username) {
		this.username = username.toLowerCase();
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}
}
