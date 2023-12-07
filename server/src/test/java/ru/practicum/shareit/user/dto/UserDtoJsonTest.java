package ru.practicum.shareit.user.dto;

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
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Value("classpath:user.json")
    Resource userResource;

    @Test
    void serializeInCorrectFormat() throws Exception {

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setEmail("updateName@user.com");
        userDto.setName("updateName");

        JsonContent<UserDto> json = jacksonTester.write(userDto);

        assertThat(json).isEqualToJson(userResource);
        assertThat(json).hasJsonPathStringValue("$.email");
        assertThat(json).extractingJsonPathStringValue("@.email")
                .isEqualTo("updateName@user.com");
        assertThat(json).extractingJsonPathStringValue("@.name")
                .isEqualTo("updateName");
    }

    @Test
    void deserializeFromCorrectFormat() throws Exception {

        String json = StreamUtils.copyToString(userResource.getInputStream(),
                Charset.defaultCharset());

        UserDto userDto = jacksonTester.parseObject(json);

        assertThat(userDto.getId()).isEqualTo(1);
        assertThat(userDto.getEmail()).isEqualTo("updateName@user.com");
        assertThat(userDto.getName()).isEqualTo("updateName");
    }
}