package gg.gamello.user.core.application.command;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class EmailChangeRequestCommand {
	@NotEmpty(message = "Email can not be empty")
	@Email(message = "Email should be correctly")
	String email;

	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}
}
