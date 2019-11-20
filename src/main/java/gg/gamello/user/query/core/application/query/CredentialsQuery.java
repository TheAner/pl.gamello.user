package gg.gamello.user.query.core.application.query;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CredentialsQuery {
	@NotEmpty(message = "Login can not be empty")
	private String login;

	@NotEmpty(message = "Password can not be empty")
	private String password;
}
