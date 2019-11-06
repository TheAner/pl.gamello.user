package gg.gamello.user.confirmation.aplication.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ConfirmationCommand {
	@NotEmpty(message = "Token can not be empty")
	String token;
}
