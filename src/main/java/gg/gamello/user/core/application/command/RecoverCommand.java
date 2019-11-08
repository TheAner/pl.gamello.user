package gg.gamello.user.core.application.command;

import gg.gamello.user.confirmation.aplication.command.ConfirmationCommand;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
public class RecoverCommand extends ConfirmationCommand {
	@NotEmpty(message = "User Id can not be empty")
	UUID userId;

	@NotEmpty(message = "Password can not be empty")
	@Length(min = 8, max = 64, message = "Password must be between {min} and {max} characters long")
	String password;
}
