package gg.gamello.user.core.application.command;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class PasswordChangeCommand {
	@NotEmpty(message = "Old password can not be empty")
	@Length(min = 8, max = 64, message = "Password must be between {min} and {max} characters long")
	String oldPassword;

	@NotEmpty(message = "New password can not be empty")
	@Length(min = 8, max = 64, message = "Password must be between {min} and {max} characters long")
	String newPassword;
}
