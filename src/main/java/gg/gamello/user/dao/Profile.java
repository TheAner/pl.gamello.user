package gg.gamello.user.dao;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(
        name = "Profile",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id","visibleName"})}
)
@Data
@NoArgsConstructor
public class Profile {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    @Length(min = 3, max = 24)
    private String visibleName;

    private String slug;

    private String avatarLocation;

    public Profile(@NotBlank UUID userId, @NotBlank String visibleName) {
        this.id = userId;
        this.visibleName = visibleName;
    }
}