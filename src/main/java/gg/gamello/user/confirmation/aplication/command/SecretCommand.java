package gg.gamello.user.confirmation.aplication.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SecretCommand {
	@NotEmpty(message = "Secret can not be empty")
	String secret;
}
