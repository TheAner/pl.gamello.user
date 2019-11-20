package gg.gamello.user.command.core.application.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ConfirmCommand {
	@NotNull(message = "UserId can not be null")
	UUID userId;
	@NotEmpty(message = "Secret can not be empty")
	String secret;
}
