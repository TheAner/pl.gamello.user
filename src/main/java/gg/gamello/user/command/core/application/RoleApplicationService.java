package gg.gamello.user.command.core.application;

import gg.gamello.user.command.core.domain.role.Role;
import gg.gamello.user.command.core.domain.role.RoleRepository;
import gg.gamello.user.command.core.domain.role.RoleType;
import org.springframework.stereotype.Service;

@Service
public class RoleApplicationService {

	private RoleRepository roleRepository;

	public RoleApplicationService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Role getUser() {
		return roleRepository.findByRole(RoleType.getDefault());
	}
}
