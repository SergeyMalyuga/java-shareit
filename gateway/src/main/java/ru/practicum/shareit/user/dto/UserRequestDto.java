package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    private int id;
    @NotBlank(message = "Name - не может быть пустым.")
    private String name;
    @NotBlank(message = "Email - не может быть пустым.")
    @Email(message = "Не правильный формат email")
    private String email;

}
