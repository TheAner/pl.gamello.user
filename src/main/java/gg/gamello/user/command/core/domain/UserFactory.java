package gg.gamello.user.command.core.domain;

import gg.gamello.user.command.avatar.AvatarService;
import gg.gamello.user.command.core.application.RoleApplicationService;
import gg.gamello.user.command.core.application.command.RegisterCommand;
import gg.gamello.user.command.core.domain.language.Language;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class UserFactory {

	private final PasswordEncoder passwordEncoder;
	private final RoleApplicationService roleService;
	private final AvatarService avatarService;

	public UserFactory(PasswordEncoder passwordEncoder, RoleApplicationService roleService,
					   AvatarService avatarService) {
		this.passwordEncoder = passwordEncoder;
		this.roleService = roleService;
		this.avatarService = avatarService;
	}

	public User create(RegisterCommand command) {
		User user = new User();
		user.setId(UUID.randomUUID());
		user.setUsername(command.getUsername());
		user.setVisibleName(command.getUsername());
		user.setEmail(command.getEmail());
		user.setPassword(passwordEncoder.encode(command.getPassword()));
		user.setRoles(Set.of(roleService.getUser()));
		user.setLanguage(Language.mapLanguage(command.getLanguage()));
		user.setAvatarLocation(avatarService.getDefaultAvatar());
		return user;
	}
}
