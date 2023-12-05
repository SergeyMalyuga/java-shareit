package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemDtoMapper;
import ru.practicum.shareit.user.UserDtoMapper;

@Component
@Getter
@Setter
public class BookingMapper {
    @Autowired
    private ItemDtoMapper itemDtoMapper;
    @Autowired
    private UserDtoMapper userDtoMapper;

    public BookingDto bookingDto(Booking booking) {
        return new BookingDto().setBooker(userDtoMapper.toUserDto(booking.getBooker()))
                .setId(booking.getId())
                .setEnd(booking.getEnd())
                .setStart(booking.getStart())
                .setItem(itemDtoMapper.itemDto(booking.getItem()))
                .setStatus(booking.getStatus())
                .setBookerId(booking.getBookerId())
                .setItemId(booking.getItemId());
    }
}
