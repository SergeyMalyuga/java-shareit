package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotBlank(message = "Поле \'name\' не может быть пустым.")
    @NotNull(message = "Поле \'name\' не может быть пустым.")
    @Column(name = "name")
    private String name;
    @NotBlank(message = "Поле \'description\' не может быть пустым.")
    @Column(name = "description")
    private String description;
    @NotNull(message = "Поле \'available\' не может быть пустым.")
    @Column(name = "available")
    private Boolean available;
    @Column(name = "owner_id")
    private int ownerId;
    @OneToMany(mappedBy = "item")
    List<Booking> bookingList = new ArrayList<>();
    @OneToMany(mappedBy = "item")
    private List<Comment> comments = new ArrayList<>();
    @Column(name = "request_id")
    private Integer requestId;
    @Transient
    private Booking lastBooking;
    @Transient
    private Booking nextBooking;
}
