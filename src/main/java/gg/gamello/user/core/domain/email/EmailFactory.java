package gg.gamello.user.core.domain.email;

import org.springframework.stereotype.Component;

@Component
public class EmailFactory {

	public Email create(String address){
		return new Email(address);
	}
}
