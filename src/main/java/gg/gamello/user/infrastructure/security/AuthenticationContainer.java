package gg.gamello.user.infrastructure.security;

import gg.gamello.dev.authentication.model.User;
import lombok.Getter;

/**
 * This class exist only for proxying gg.gamello.dev.authentication.model.User,
 * it has only design purpose
 */
@Getter
public class AuthenticationContainer {
	User user;

	public static AuthenticationContainer contain(User user){
		var container = new AuthenticationContainer();
		container.user = user;
		return container;
	}
}
