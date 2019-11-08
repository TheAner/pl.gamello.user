package gg.gamello.user.core.application.command;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class EmailChangeRequestCommand {
	@NotEmpty(message = "EmailRequest can not be empty")
	@Email(message = "EmailRequest should be correctly")
	String email;
}
