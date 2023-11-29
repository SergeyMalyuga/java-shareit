package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor

@Accessors(chain = true)
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
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User booker;
    @Transient
    int bookerId;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (this.getClass() != object.getClass()) return false;
        Booking booking = (Booking) object;
        return this.getId() == booking.getId() && Objects.equals(this.start, booking.start)
                && Objects.equals(this.end, booking.end) && this.getItemId() == booking.getItemId()
                && Objects.equals(this.status, booking.status) && Objects.equals(this.item, booking.item)
                && Objects.equals(this.booker, booking.booker) && this.booker == booking.booker;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (id == 0) {
            hash = hash + id;
        }
        hash = hash * 31;
        if (start != null) {
            hash = hash + start.hashCode();
        }
        hash = hash * 31;
        if (end != null) {
            hash = hash + end.hashCode();
        }
        hash = hash * 31;
        if (itemId != 0) {
            hash = hash + itemId;
        }
        hash = hash * 31;
        if (status != null) {
            hash = hash + status.hashCode();
        }
        hash = hash * 31;
        if (item != null) {
            hash = hash + item.hashCode();
        }
        hash = hash * 31;
        if (booker != null) {
            hash = hash + booker.hashCode();
        }
        hash = hash * 31;
        if (bookerId != 0) {
            hash = hash + bookerId;
        }
        return hash;
    }
}
