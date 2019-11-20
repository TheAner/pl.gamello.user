package gg.gamello.user.command.core.domain.role;

public enum RoleType {
	ROLE_USER(0),
	ROLE_ADMIN(1);

	public static RoleType getDefault() {
		return ROLE_USER;
	}

	RoleType(Integer privilegeLevel) {
	}
}
