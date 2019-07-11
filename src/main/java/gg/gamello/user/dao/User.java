package gg.gamello.user.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gg.gamello.user.dao.type.Language;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.Authentication;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(
        name = "User",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email", "slug"})}
)
@Data @NoArgsConstructor
public class User {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @NotEmpty(message = "Login can not be empty")
    @Length(min = 3, max = 24, message = "Login must be between {min} and {max} characters long")
    String username;

    @NotEmpty(message = "Visible name can not be empty")
    @Length(min = 3, max = 24, message = "Visible name must be between {min} and {max} characters long")
    private String visibleName;

    private String slug;

    @NotEmpty(message = "EmailRequest can not be empty")
    @Email(message = "EmailRequest should be correctly")
    private String email;

    @JsonIgnore
    @NotEmpty(message = "Password can not be empty")
    @Length(min = 8, max = 64, message = "Password must be between {min} and {max} characters long")
    private String password;

    private boolean active = false;

    private Date registerDate = new Date();

    private String avatarLocation;

    @Enumerated(EnumType.STRING)
    private Language language;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinTable(name = "User_Role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public User(@NotBlank String username, @NotBlank String email) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.visibleName = username;
        this.email = email;
    }

    public static User getFromAuthentication(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
