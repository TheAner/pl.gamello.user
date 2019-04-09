package gg.gamello.user.dao.loader;

import gg.gamello.user.dao.Role;
import gg.gamello.user.dao.type.RoleType;
import gg.gamello.user.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Stream.of(RoleType.values()).forEach(this::createRoleIfNotFound);
    }

    @Transactional
    void createRoleIfNotFound(RoleType roleType) {
        if(!roleRepository.existsByRole(roleType))
            roleRepository.save(new Role(roleType));
    }
}
