package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoDataFoundException;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestDtoMapper;
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
    private RequestRepository requestRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestDtoMapper itemRequestDtoMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestDtoMapper requestDtoMapper;
    @Autowired
    private RequestMapper requestMapper;

    @Override
    public RequestDto addRequest(RequestDto requestDto, int userId) {
        Request request = requestMapper.toRequest(requestDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException("Пользователь с id:" + userId + " не найден."));
        request.setRequesterId(userId);
        request.setCreated(LocalDateTime.now());
        return requestDtoMapper.toRequestDto(requestRepository.save(request));
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
            requestDtoList.add(requestDtoMapper.toRequestDto(request).setItems(itemRequestDtoList));
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
        return requestDtoMapper.toRequestDto(request).setItems(itemRequestDtoList);
    }

    @Override
    public List<RequestDto> getAllRequests(int userId, Optional<Integer> from, Optional<Integer> size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException("Пользователь с id:" + userId + " не найден."));
        if (from.isPresent() && size.isPresent()) {
            List<RequestDto> requestDtoList = new ArrayList<>();
            List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
            List<Request> requestList;
            if (from.isPresent() && from.get() >= 0 && size.isPresent() && size.get() > 0) {
                requestList = requestRepository
                        .findAllRequests(userId, PageRequest.of((int) Math.ceil((double) from.get() / size.get()),
                                size.get()))
                        .getContent();
            } else {
                throw new UnavailableItemException("Не допустимое значение.");
            }
            for (Request request : requestList) {
                itemRequestDtoList.addAll(itemRepository.findByRequestId(request.getId())
                        .stream().map(e -> itemRequestDtoMapper.toItemRequestDto(e)).collect(Collectors.toList()));
                requestDtoList.add(requestDtoMapper.toRequestDto(request).setItems(itemRequestDtoList));
            }
            return requestDtoList;
        } else {
            return new ArrayList<>();
        }
    }
}
