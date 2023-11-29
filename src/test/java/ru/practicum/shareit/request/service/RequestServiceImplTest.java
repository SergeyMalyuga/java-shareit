package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    private RequestService requestService;
    private Request request = new Request();
    private Request request2 = new Request();
    private Request request3 = new Request();
    @Autowired
    private RequestMapper requestMapper;
    List<Request> requestList = new ArrayList<>();

    @BeforeEach
    void setUp() {

        request.setId(1).setDescription("описание").setRequesterId(100).setCreated(LocalDateTime.now());
        request2.setId(2).setDescription("описание2").setRequesterId(200).setCreated(LocalDateTime.now());
        request3.setId(3).setDescription("описание3").setRequesterId(300).setCreated(LocalDateTime.now());
        Collections.addAll(requestList, request, request2, request3);
        requestMapper = new RequestMapper();
    }


    @Test
    void addRequest() {
        Mockito.when(requestService.addRequest(Mockito.any(Request.class), Mockito.anyInt()))
                .thenReturn(requestMapper.toRequestDto(request));
        RequestDto newRequest = requestService.addRequest(request, 1);
        assertEquals(request.getId(), newRequest.getId());
    }

    @Test
    void getRequestsList() {
        Mockito.when(requestService.getRequestsList(Mockito.anyInt())).thenReturn(requestList.stream()
                .map(e -> requestMapper.toRequestDto(e)).collect(Collectors.toList()));
        List<RequestDto> newRequestList = requestService.getRequestsList(1);
        assertEquals(requestList.size(), newRequestList.size());
    }

    @Test
    void getRequestById() {
        Mockito.when(requestService.getRequestById(Mockito.anyInt(), Mockito.anyInt())).thenReturn(requestMapper.toRequestDto(request2));
        RequestDto newRequest = requestService.getRequestById(1, 2);
        assertEquals(request2.getId(), newRequest.getId());
    }

    @Test
    void getAllRequests() {
        Mockito.when(requestService.getAllRequests(Mockito.anyInt(), Mockito.any(Optional.class),
                Mockito.any(Optional.class))).thenReturn(requestList.stream()
                .map(e -> requestMapper.toRequestDto(e)).collect(Collectors.toList()));
        List<RequestDto> newRequestList = requestService.getAllRequests(1, Optional.of(1),
                Optional.of(1));
        assertEquals(requestList.size(), newRequestList.size());
    }
}