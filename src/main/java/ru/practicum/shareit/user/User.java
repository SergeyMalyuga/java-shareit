package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotBlank(message = "Name - не может быть пустым.")
    @Column(name = "name")
    private String name;
    @NotBlank(message = "Email - не может быть пустым.")
    @Email(message = "Не правильный формат email")
    @Column(name = "email", unique = true)
    private String email;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (this.getClass() != object.getClass()) return false;
        User user = (User) object;
        return this.id == user.id && Objects.equals(this.name, user.name) && Objects.equals(this.email, user.email);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (id != 0) {
            hash = hash + id;
        }
        hash = hash * 31;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 31;
        if (email != null) {
            hash = hash + email.hashCode();
        }
        return hash;
    }
}
