package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.RequestDto;

@Component
public class RequestMapper {

    public Request toRequest(RequestDto request) {
        return new Request().setDescription(request.getDescription());
    }
}
