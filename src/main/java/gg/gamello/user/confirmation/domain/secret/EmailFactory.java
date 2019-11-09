package gg.gamello.user.confirmation.domain.secret;

import gg.gamello.user.confirmation.infrastructure.properties.EmailProperties;
import org.apache.commons.lang.RandomStringUtils;
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
			token.append(RandomStringUtils.randomAlphanumeric(emailProperties.getToken().getChars()));

			if (i != (emailProperties.getToken().getSections()-1)) {
				token.append("-");
			}
		}

		return token.toString();
	}
}