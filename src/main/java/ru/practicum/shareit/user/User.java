package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
}
