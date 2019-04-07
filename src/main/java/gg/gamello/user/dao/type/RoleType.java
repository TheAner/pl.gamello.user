package gg.gamello.user.dao.type;

import gg.gamello.user.dao.Role;

import java.util.HashSet;
import java.util.Set;

public enum RoleType {
    USER(0),
    ADMIN(1);

    private final Integer privilegeLevel;

    RoleType(Integer privilegeLevel) {
        this.privilegeLevel = privilegeLevel;
    }

// Java 1.9
//    public static Set<Role> getDefaultRoles() {
//        return Set.of(new Role(RoleType.USER));
//    }

    public static Set<Role> getDefaultRoles() {
        return new HashSet<Role>() {{ add(new Role(RoleType.USER)); }};
    }
}
