package gg.gamello.user.command.core.application.loader;

import gg.gamello.user.command.core.domain.role.RoleFactory;
import gg.gamello.user.command.core.domain.role.RoleRepository;
import gg.gamello.user.command.core.domain.role.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Slf4j
@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private RoleRepository roleRepository;

	private RoleFactory roleFactory;

	public InitialDataLoader(RoleRepository roleRepository, RoleFactory roleFactory) {
		this.roleRepository = roleRepository;
		this.roleFactory = roleFactory;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Stream.of(RoleType.values()).forEach(this::createRoleIfNotFound);
	}

	@Transactional
	void createRoleIfNotFound(RoleType roleType) {
		if (!roleRepository.existsByRole(roleType))
			roleRepository.save(roleFactory.create(roleType));
	}
}