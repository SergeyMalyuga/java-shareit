package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    @OneToMany(mappedBy = "item")
    List<Booking> bookingList = new ArrayList<>();
    @OneToMany(mappedBy = "item")
    private List<Comment> comments = new ArrayList<>();
    @Transient
    private Booking lastBooking;
    @Transient
    private Booking nextBooking;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (this.getClass() != object.getClass()) return false;
        Item item = (Item) object;
        return this.id == item.id && Objects.equals(this.name, item.name)
                && Objects.equals(this.description, item.description)
                && Objects.equals(this.available, item.available)
                && this.ownerId == item.ownerId && Objects.equals(this.request, item.request)
                && Objects.equals(this.bookingList, item.bookingList)
                && Objects.equals(this.comments, item.comments)
                && Objects.equals(this.lastBooking, item.lastBooking)
                && Objects.equals(this.nextBooking, item.nextBooking);
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
        if (description != null) {
            hash = hash + description.hashCode();
        }
        hash = hash * 31;
        if (available != null) {
            hash = hash + available.hashCode();
        }
        hash = hash * 31;
        if (ownerId != 0) {
            hash = hash + ownerId;
        }
        hash = hash * 31;
        if (bookingList != null) {
            hash = hash + bookingList.hashCode();
        }
        hash = hash * 31;
        if (comments != null) {
            hash = hash + comments.hashCode();
        }
        hash = hash * 31;
        if (lastBooking != null) {
            hash = hash + lastBooking.hashCode();
        }
        hash = hash * 31;
        if (nextBooking != null) {
            hash = hash + nextBooking.hashCode();
        }
        return hash;
    }
}
