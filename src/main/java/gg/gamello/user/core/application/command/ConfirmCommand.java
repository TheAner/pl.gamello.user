package gg.gamello.user.core.application.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
public class ConfirmCommand {
	@NotEmpty(message = "UserId can not be empty")
	UUID userId;
	@NotEmpty(message = "Secret can not be empty")
	String secret;
}
