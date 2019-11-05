package gg.gamello.user.core.domain.email;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(
		name = "email"
)
@NoArgsConstructor @Getter
public class Email {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	private String address;

	@Enumerated(EnumType.STRING)
	private Status status;

	private Date added;

	public enum Status {
		PRIMARY,
		VERIFIED,
		UNVERIFIED
	}

	Email(String address) {
		this.id = UUID.randomUUID();
		this.address = address;
		this.status = Status.UNVERIFIED;
		this.added = new Date();
	}
}
