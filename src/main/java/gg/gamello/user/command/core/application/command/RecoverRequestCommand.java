package gg.gamello.user.command.core.application.command;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class RecoverRequestCommand {
	@NotEmpty(message = "Email can not be empty")
	@Email(message = "Email should be correctly")
	String email;
}
