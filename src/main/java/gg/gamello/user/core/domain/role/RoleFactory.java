package gg.gamello.user.core.domain.role;

import org.springframework.stereotype.Component;

@Component
public class RoleFactory {

	public Role create(RoleType roleType){
		return new Role(roleType);
	}
}
