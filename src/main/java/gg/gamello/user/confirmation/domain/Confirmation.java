package gg.gamello.user.confirmation.domain;

import gg.gamello.user.confirmation.domain.action.ActionType;
import gg.gamello.user.confirmation.domain.method.MethodType;
import gg.gamello.user.confirmation.infrastructure.exception.IncorrectSecretException;
import gg.gamello.user.confirmation.infrastructure.exception.OutdatedConfirmationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Domain Aggregate Root
 */
@Entity
@Table(
		name = "confirmation"
)
@NoArgsConstructor @Getter @Setter(AccessLevel.PACKAGE)
public class Confirmation extends AbstractAggregateRoot<Confirmation> {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@NotNull
	@Column(columnDefinition = "BINARY(16)")
	private UUID userId;

	@Enumerated(EnumType.STRING)
	private ActionType actionType;

	@Enumerated(EnumType.STRING)
	private MethodType methodType;

	private String secret;

	private Date expiration;

	private String attachment;

	public void check(String secret) throws OutdatedConfirmationException, IncorrectSecretException {
		if (expiration.compareTo(new Date())<0)
			throw new OutdatedConfirmationException("Confirmation has expired");
		if (!secret.equals(this.secret))
			throw new IncorrectSecretException("Invalid secret");
	}
}
