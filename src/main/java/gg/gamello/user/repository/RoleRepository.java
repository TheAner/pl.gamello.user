package gg.gamello.user.repository;

import gg.gamello.user.dao.Role;
import gg.gamello.user.dao.type.RoleType;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(RoleType role);
    boolean existsByRole(RoleType role);
}
