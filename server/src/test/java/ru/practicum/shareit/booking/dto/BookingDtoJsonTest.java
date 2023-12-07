package ru.practicum.shareit.booking.dto;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingDtoJsonTest {
    @Autowired
    private JacksonTester<BookingDto> jacksonTester;

    @Value("classpath:booking.json")
    Resource bookingResource;

    @Test
    void serializeInCorrectFormat() throws Exception {

        ItemDto itemDto = new ItemDto();
        itemDto.setId(2).setName("Отвертка").setDescription("Аккумуляторная отвертка").setOwnerId(4).setAvailable(true);
        UserDto userDto = new UserDto();
        userDto.setId(1).setName("updateName").setEmail("updateName@user.com");

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(2)
                .setStart(LocalDateTime.of(2023, 11, 29, 13, 52, 38))
                .setEnd(LocalDateTime.of(2023, 11, 30, 13, 52, 38))
                .setItem(itemDto).setBooker(userDto).setStatus(BookingStatus.APPROVED);

        JsonContent<BookingDto> json = jacksonTester.write(bookingDto);

        assertThat(json).isEqualToJson(bookingResource);
        assertThat(json).hasJsonPathStringValue("$.item.description");
        assertThat(json).extractingJsonPathStringValue("@.item.name")
                .isEqualTo("Отвертка");
        assertThat(json).extractingJsonPathStringValue("@.item.description")
                .isEqualTo("Аккумуляторная отвертка");
    }

    @Test
    void deserializeFromCorrectFormat() throws Exception {

        String json = StreamUtils.copyToString(bookingResource.getInputStream(),
                Charset.defaultCharset());

        BookingDto bookingDto = jacksonTester.parseObject(json);

        AssertionsForClassTypes.assertThat(bookingDto.getId()).isEqualTo(2);
        AssertionsForClassTypes.assertThat(bookingDto.getItem().getName()).isEqualTo("Отвертка");
        AssertionsForClassTypes.assertThat(bookingDto.getItem().getDescription())
                .isEqualTo("Аккумуляторная отвертка");
    }

}