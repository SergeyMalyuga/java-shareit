package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.RequestDto;

@Component
public class RequestMapper {
    public RequestDto toRequestDto(Request request) {
        return new RequestDto().setId(request.getId())
                //.setRequesterId(request.getRequesterId())
                .setDescription(request.getDescription())
                .setCreated(request.getCreated());
    }
}
