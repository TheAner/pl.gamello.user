package gg.gamello.user.command.core.domain;

import gg.gamello.user.command.core.domain.language.Language;
import gg.gamello.user.command.core.domain.role.Role;
import gg.gamello.user.command.core.infrastructure.exception.PasswordsDontMatchException;
import gg.gamello.user.command.core.infrastructure.exception.UserIsNotActiveException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Domain Aggregate Root
 */
@Entity
@Table(
		name = "u_user",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email", "slug"})}
)
@NoArgsConstructor
@Getter
@Setter(AccessLevel.PACKAGE)
public class User extends AbstractAggregateRoot<User> implements Serializable {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	private String username;

	private String visibleName;

	private String slug;

	private String email;

	private String password;

	private boolean active = false;

	private Date registered = new Date();

	private String avatarLocation;

	@Enumerated(EnumType.STRING)
	private Language language;

	@ManyToMany(fetch = FetchType.EAGER,
			cascade = CascadeType.PERSIST)
	@JoinTable(name = "u_user_role",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Set<Role> roles;

	public void activate() {
		this.setActive(true);
	}

	public void checkActive() throws UserIsNotActiveException {
		if (!this.isActive())
			throw new UserIsNotActiveException("User is not active");
	}

	public void changePassword(String password, PasswordEncoder encoder) {
		this.setPassword(encoder.encode(password));
	}

	public void matchPassword(String password, PasswordEncoder encoder) throws PasswordsDontMatchException {
		if (!encoder.matches(password, this.password))
			throw new PasswordsDontMatchException("Passwords don't match");
	}

	public void changeEmail(String email) {
		this.setEmail(email);
	}

	public void changeLanguage(String language) {
		this.setLanguage(Language.mapLanguage(language));
	}

	public void changeSlug(String slug) {
		this.setSlug(slug);
	}

	public void changeVisibleName(String visibleName) {
		this.setVisibleName(visibleName);
	}

	public void changeAvatarLocation(String location) {
		this.setAvatarLocation(location);
	}
}
