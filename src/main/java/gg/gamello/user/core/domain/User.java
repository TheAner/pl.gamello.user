package gg.gamello.user.core.domain;

import gg.gamello.user.core.domain.language.Language;
import gg.gamello.user.core.domain.role.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * Domain Aggregate Root
 */
@Entity
@Table(
		name = "user",
		uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "slug"})}
)
@NoArgsConstructor @Getter @Setter(AccessLevel.PACKAGE)
public class User extends AbstractAggregateRoot<User> {

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
			cascade = CascadeType.ALL)
	@JoinTable(name = "user_role",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;
}
