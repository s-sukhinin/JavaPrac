package ru.msu.cs.javaprac.models;

import java.sql.Timestamp;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User implements CommonEntity{

    public enum RoleType
    {
        MODERATOR,
        USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "user_id")
    private Integer id;

    @Column(nullable = false, name = "login")
    @NonNull
    private String login;

    @Column(nullable = false, name = "password")
    @NonNull
    private String password;

    @Column(nullable = false, name = "name")
    @NonNull
    private String name;

    @Column(name = "date_of_registration")
    private Timestamp dateOfRegistration;

    @Column(nullable = false, name = "role")
    @NonNull
    private RoleType role;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(role, user.role) &&
                Objects.equals(dateOfRegistration, user.dateOfRegistration);
    }
}
