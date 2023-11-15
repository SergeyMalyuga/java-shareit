package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

@Component
public class BookingMapper {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private UserMapper userMapper;

    public BookingDto bookingDto(Booking booking) {
        return new BookingDto().setBooker(userMapper.toUserDto(booking.getBooker()))
                .setId(booking.getId())
                .setEnd(booking.getEnd())
                .setStart(booking.getStart())
                .setItem(itemMapper.itemDto(booking.getItem()))
                .setStatus(booking.getStatus())
                .setBookerId(booking.getBookerId())
                .setItemId(booking.getItemId());
    }
}
