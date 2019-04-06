package gg.gamello.user.domain;

import lombok.Data;

@Data
public class UserRegistrationForm {
    String username;
    String email;
    String password;
}
