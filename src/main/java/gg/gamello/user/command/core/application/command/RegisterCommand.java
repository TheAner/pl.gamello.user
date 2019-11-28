package gg.gamello.user.command.core.application.command;

import gg.gamello.user.infrastructure.validation.PasswordPolicy;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterCommand {
	@NotEmpty(message = "Username can not be empty")
	@Size(min = 3, max = 24, message = "Username must be between {min} and {max} characters long")
	@Pattern(regexp = "^[a-z0-9]*$", message = "Username must be lower case, letters and digits only")
	String username;

	@NotEmpty(message = "Email can not be empty")
	@Email(message = "Email should be correctly")
	String email;

	@NotEmpty(message = "Password can not be empty")
	@PasswordPolicy
	String password;

	@NotEmpty(message = "Language can not be empty")
	String language;

	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}
}
