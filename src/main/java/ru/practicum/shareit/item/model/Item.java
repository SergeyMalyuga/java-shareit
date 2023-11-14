package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode
@ToString
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
    @Column(name = "request")
    private String request;
    @JsonIgnore
    @OneToMany(mappedBy = "item")
    List<Booking> bookingList = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "item")
    private List<Comment> comments = new ArrayList<>();
    @Transient
    private Booking lastBooking;
    @Transient
    private Booking nextBooking;
}
