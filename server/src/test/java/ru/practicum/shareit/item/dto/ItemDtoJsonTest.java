package ru.practicum.shareit.item.dto;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> jacksonTester;

    @Value("classpath:item.json")
    Resource itemResource;

    @Test
    void serializeInCorrectFormat() throws Exception {
        ItemDto itemDto = new ItemDto().setId(1).setAvailable(true).setDescription("Аккумуляторная дрель + аккумулятор")
                .setName("Аккумуляторная дрель").setOwnerId(1);

        JsonContent<ItemDto> json = jacksonTester.write(itemDto);

        assertThat(json).isEqualToJson(itemResource);
        assertThat(json).hasJsonPathStringValue("$.description");
        assertThat(json).extractingJsonPathStringValue("$.description")
                .isEqualTo("Аккумуляторная дрель + аккумулятор");
    }

    @Test
    void deserializeFromCorrectFormat() throws Exception {
        String json = StreamUtils.copyToString(itemResource.getInputStream(),
                Charset.defaultCharset());

        ItemDto itemDto = jacksonTester.parseObject(json);

        AssertionsForClassTypes.assertThat(itemDto.getId()).isEqualTo(1);
        AssertionsForClassTypes.assertThat(itemDto.getDescription())
                .isEqualTo("Аккумуляторная дрель + аккумулятор");
    }
}