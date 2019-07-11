package gg.gamello.user.domain.auth;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class Credentials {
    @NotEmpty(message = "Login can not be empty")
    private String login;

    @NotEmpty(message = "Password can not be empty")
    @Length(min = 8, max = 64, message = "Password must be between {min} and {max} characters long")
    private String password;
}
