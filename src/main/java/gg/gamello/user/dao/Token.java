package gg.gamello.user.dao;

import gg.gamello.user.dao.type.TokenType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(
        name = "Token"
)
@Data
@NoArgsConstructor
public class Token {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @NotNull
    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private TokenType typeOfToken;

    private Date tokenExpirationDate;

    @NotBlank
    private String value;

    public Token(UUID userId, TokenType typeOfToken, @NotBlank String value) {
        this.userId = userId;
        this.typeOfToken = typeOfToken;
        this.value = value;
    }
}
