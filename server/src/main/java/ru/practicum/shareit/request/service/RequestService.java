package ru.practicum.shareit.request.service;


import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;
import java.util.Optional;

public interface RequestService {
    RequestDto addRequest(RequestDto requestDto, int userId);

    List<RequestDto> getRequestsList(int userId);

    RequestDto getRequestById(int requestId, int userId);

    List<RequestDto> getAllRequests(int userId, Optional<Integer> from, Optional<Integer> size);
}
