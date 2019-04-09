package gg.gamello.user.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(
        name = "User",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"username","email"})}
)
@Data @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(min = 3, max = 24)
    private String username;

    @NotBlank
    @Email
    private String email;

    @JsonIgnore
    @NotBlank
    private String password;

    private boolean active = false;

    private Date registerDate = new Date();

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinTable(name = "User_Role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public User(@NotBlank String username, @NotBlank String email) {
        this.username = username;
        this.email = email;
    }

}
