package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.RequestDto;

@Component
public class RequestDtoMapper {
    public RequestDto toRequestDto(Request request) {
        return new RequestDto().setId(request.getId())
                .setDescription(request.getDescription())
                .setCreated(request.getCreated());
    }
}
