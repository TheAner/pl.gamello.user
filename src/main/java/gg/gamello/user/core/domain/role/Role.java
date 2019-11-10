package gg.gamello.user.core.domain.role;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(
		name = "role"
)
@Getter
@NoArgsConstructor
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private RoleType role;

	Role(RoleType role) {
		this.role = role;
	}
}
