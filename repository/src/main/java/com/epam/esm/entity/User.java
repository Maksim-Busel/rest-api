package com.epam.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"username"})
@ToString(of = {"id","username"})
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@NamedQueries({
        @NamedQuery(name = User.QueryNames.FIND_ALL,
                    query = "SELECT u FROM users u WHERE lock=false ORDER BY id"),
        @NamedQuery(name = User.QueryNames.FIND_BY_ID,
                    query = "SELECT u FROM users u WHERE id=:userId AND lock=false"),
        @NamedQuery(name = User.QueryNames.LOCK_BY_ID,
                    query = "UPDATE users SET lock=true WHERE id=:userId"),
        @NamedQuery(name = User.QueryNames.FIND_BY_USERNAME,
                    query = "SELECT u FROM users u WHERE username=:username AND lock=false"),
})
public class User implements Identifable{

    @Id
    @SequenceGenerator(name="user_id_seq", sequenceName = "user_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    public static final class QueryNames {
        public static final String FIND_BY_ID = "User.findById";
        public static final String FIND_ALL = "User.findAll";
        public static final String LOCK_BY_ID = "User.lockById";
        public static final String FIND_BY_USERNAME = "User.findByUsername";

        public QueryNames() {
        }
    }
}
