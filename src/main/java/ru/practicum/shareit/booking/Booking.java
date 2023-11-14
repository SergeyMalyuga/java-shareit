package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "start_time")
    private LocalDateTime start;
    @Column(name = "end_time")
    private LocalDateTime end;
    @Transient
    private int itemId;
    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User booker;
    @Transient
    int bookerId;
}
