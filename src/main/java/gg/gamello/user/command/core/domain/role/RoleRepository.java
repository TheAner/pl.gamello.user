package gg.gamello.user.command.core.domain.role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByRole(RoleType role);

	boolean existsByRole(RoleType role);
}
