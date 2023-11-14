package ru.practicum.shareit.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
@Component
@Entity
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
    @Column(name = "email")
    private String email;
}
