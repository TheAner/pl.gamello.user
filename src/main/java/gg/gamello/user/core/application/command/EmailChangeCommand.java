package gg.gamello.user.core.application.command;

import gg.gamello.user.confirmation.aplication.command.SecretCommand;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
public class EmailChangeCommand extends SecretCommand {
	@NotEmpty(message = "User Id can not be empty")
	UUID userId;
}
