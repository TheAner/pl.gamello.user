package gg.gamello.user.dao;

import gg.gamello.user.dao.type.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(
        name = "Role"
)
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    public Role(RoleType role) {
        this.role = role;
    }
}
