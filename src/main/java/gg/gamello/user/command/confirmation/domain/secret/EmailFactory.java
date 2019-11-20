package gg.gamello.user.command.confirmation.domain.secret;

import gg.gamello.user.command.confirmation.infrastructure.properties.EmailProperties;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailFactory implements SecretFactory {

	@Autowired
	EmailProperties emailProperties;

	@Override
	public String create() {
		StringBuilder token = new StringBuilder();
		int sections = emailProperties.getToken().getSections();
		int chars = emailProperties.getToken().getChars();

		for (int i = 0; i < emailProperties.getToken().getSections(); i++) {
			token.append(RandomString.make(emailProperties.getToken().getChars()));

			if (i != (emailProperties.getToken().getSections() - 1)) {
				token.append("-");
			}
		}

		return token.toString();
	}
}
