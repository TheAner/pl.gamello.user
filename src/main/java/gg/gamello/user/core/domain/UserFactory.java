package gg.gamello.user.core.domain;

import gg.gamello.user.core.application.RoleApplicationService;
import gg.gamello.user.core.application.command.RegisterUserCommand;
import gg.gamello.user.core.domain.email.EmailFactory;
import gg.gamello.user.core.domain.language.Language;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class UserFactory {

	private EmailFactory emailFactory;

	private PasswordEncoder passwordEncoder;

	private RoleApplicationService roleService;

	public UserFactory(EmailFactory emailFactory, PasswordEncoder passwordEncoder, RoleApplicationService roleService) {
		this.emailFactory = emailFactory;
		this.passwordEncoder = passwordEncoder;
		this.roleService = roleService;
	}

	public User create(RegisterUserCommand command) {
		User user = new User();
		user.setId(UUID.randomUUID());
		user.setUsername(command.getUsername());
		user.setVisibleName(command.getUsername());
		user.setEmails(Set.of(emailFactory.create(command.getEmail())));
		user.setPassword(passwordEncoder.encode(command.getPassword()));
		user.setRoles(Set.of(roleService.getUser()));
		user.setLanguage(Language.mapLanguage(command.getLanguage()));
		user.setAvatarLocation(""); //todo: set default avatar
		return user;
	}
}
