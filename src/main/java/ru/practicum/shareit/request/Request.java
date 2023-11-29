package ru.practicum.shareit.request;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "description")
    @NotBlank(message = "Поле не может быть пустым!")
    @NotNull(message = "Поле не может быть пустым!")
    private String description;
    @Column(name = "requester_id")
    private int requesterId;
    @Column(name = "created")
    private LocalDateTime created;
}
