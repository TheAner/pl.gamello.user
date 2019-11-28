package gg.gamello.user.query.core.application.query;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UsernameCheckQuery {
	@NotEmpty(message = "Username can not be empty")
	@Size(min = 3, max = 24, message = "Username must be between {min} and {max} characters long")
	@Pattern(regexp = "^[a-z0-9]*$", message = "Username must be lower case, letters and digits only")
	String username;
}
