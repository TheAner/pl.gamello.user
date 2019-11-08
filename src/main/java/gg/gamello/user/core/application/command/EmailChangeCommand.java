package gg.gamello.user.core.application.command;

import gg.gamello.user.confirmation.aplication.command.ConfirmationCommand;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
public class EmailChangeCommand extends ConfirmationCommand {
	@NotEmpty(message = "User Id can not be empty")
	UUID userId;
}
