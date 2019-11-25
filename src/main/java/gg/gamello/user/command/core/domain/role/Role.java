package gg.gamello.user.command.core.domain.role;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(
		name = "u_role"
)
@Getter
@NoArgsConstructor
public class Role implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private RoleType role;

	Role(RoleType role) {
		this.role = role;
	}
}
