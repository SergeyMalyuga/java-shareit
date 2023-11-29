package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoDataFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    private ItemRequestDtoMapper itemRequestDtoMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestMapper requestMapper;

    @Override
    public RequestDto addRequest(Request request, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException("Пользователь с id:" + userId + " не найден."));
        request.setRequesterId(userId);
        request.setCreated(LocalDateTime.now());
        requestRepository.save(request);
        return requestMapper.toRequestDto(request);
    }

    @Override
    public List<RequestDto> getRequestsList(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException("Пользователь с id:" + userId + " не найден."));
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        List<RequestDto> requestDtoList = new ArrayList<>();
        List<Request> requestList = requestRepository.findByRequesterId(userId);
        for (Request request : requestList) {
            itemRequestDtoList.addAll(itemRepository.findByRequestId(request.getId())
                    .stream().map(e -> itemRequestDtoMapper.toItemRequestDto(e)).collect(Collectors.toList()));
            requestDtoList.add(requestMapper.toRequestDto(request).setItems(itemRequestDtoList));
        }
        return requestDtoList;
    }

    @Override
    public RequestDto getRequestById(int requestId, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException("Пользователь с id:" + userId + " не найден."));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoDataFoundException("Запроса с id:" + requestId + " не существует"));
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        itemRequestDtoList.addAll(itemRepository.findByRequestId(request.getId())
                .stream().map(e -> itemRequestDtoMapper.toItemRequestDto(e)).collect(Collectors.toList()));
        return requestMapper.toRequestDto(request).setItems(itemRequestDtoList);
    }

    @Override
    public List<RequestDto> getAllRequests(int userId, Optional<Integer> from, Optional<Integer> size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException("Пользователь с id:" + userId + " не найден."));
        if (from.isPresent() && size.isPresent()) {
            List<RequestDto> requestDtoList = new ArrayList<>();
            List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
            List<Request> requestList = requestRepository
                    .findAllRequests(userId, PageRequest.of(from.get(), size.get()))
                    .getContent();
            for (Request request : requestList) {
                itemRequestDtoList.addAll(itemRepository.findByRequestId(request.getId())
                        .stream().map(e -> itemRequestDtoMapper.toItemRequestDto(e)).collect(Collectors.toList()));
                requestDtoList.add(requestMapper.toRequestDto(request).setItems(itemRequestDtoList));
            }
            return requestDtoList;
        } else {
            return new ArrayList<>();
        }
    }
}
