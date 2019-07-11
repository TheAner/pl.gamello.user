package gg.gamello.user.domain.auth;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserRegistrationForm {
    @NotEmpty(message = "Username can not be empty")
    @Length(min = 3, max = 24, message = "Username must be between {min} and {max} characters long")
    String username;

    @NotEmpty(message = "EmailRequest can not be empty")
    @Email(message = "EmailRequest should be correctly")
    String email;

    @NotEmpty(message = "Password can not be empty")
    @Length(min = 8, max = 64, message = "Password must be between {min} and {max} characters long")
    String password;

    @NotEmpty(message = "Language can not be empty")
    String language;
}
