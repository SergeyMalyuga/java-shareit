package ru.practicum.shareit.request.dto;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class RequestDtoJsonTest {

    @Autowired
    private JacksonTester<RequestDto> jacksonTester;

    @Value("classpath:resource.json")
    Resource requestResource;

    @Test
    void serializeInCorrectFormat() throws Exception {

        RequestDto requestDto = new RequestDto();
        requestDto.setId(1);
        requestDto.setDescription("Хотел бы воспользоваться щёткой для обуви");
        requestDto.setCreated(LocalDateTime.of(2023, 11, 24, 12, 29, 45));
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(5)
                .setName("Щётка для обуви")
                .setDescription("Стандартная щётка для обуви")
                .setAvailable(true).setRequestId(1);

        requestDto.setItems(List.of(itemRequestDto));

        JsonContent<RequestDto> json = jacksonTester.write(requestDto);

        assertThat(json).isEqualToJson(requestResource);

        assertThat(json).hasJsonPathStringValue("$.description");
        assertThat(json).extractingJsonPathStringValue("$.description")
                .isEqualTo("Хотел бы воспользоваться щёткой для обуви");
        assertThat(json).extractingJsonPathArrayValue("$.items").hasSize(1);
        assertThat(json).extractingJsonPathArrayValue("$.items").isNotEmpty();
    }

    @Test
    void deserializeFromCorrectFormat() throws Exception {

        String json = StreamUtils.copyToString(requestResource.getInputStream(),
                Charset.defaultCharset());

        RequestDto requestDto = jacksonTester.parseObject(json);

        AssertionsForClassTypes.assertThat(requestDto.getId()).isEqualTo(1);
        AssertionsForClassTypes.assertThat(requestDto.getDescription())
                .isEqualTo("Хотел бы воспользоваться щёткой для обуви");
        AssertionsForClassTypes.assertThat(requestDto.getCreated())
                .isEqualTo(LocalDateTime.of(2023, 11, 24, 12, 29, 45));
    }

}