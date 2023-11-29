package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @InjectMocks
    private RequestService requestService = new RequestServiceImpl();
    @Mock
    RequestRepository requestRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private ItemRequestDtoMapper itemRequestDtoMapper;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    RequestMapper requestMapper;
    private Request request = new Request();
    private Request request2 = new Request();
    private Request request3 = new Request();
    private User user;
    private Item item;
    List<Request> requestList = new ArrayList<>();
    List<Item> itemList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        user = new User().setId(1).setEmail("serg@mail.ru").setName("Sergey");
        item = new Item().setId(1).setAvailable(true).setDescription("Аккумуляторная дрель")
                .setName("Дрель").setOwnerId(1);
        request.setId(1).setDescription("описание");
        request2.setId(2).setDescription("описание2");
        request3.setId(3).setDescription("описание3");
        Collections.addAll(requestList, request, request2, request3);
        Collections.addAll(itemList, item);
        requestMapper = new RequestMapper();
    }


    @Test
    void addRequest_Should_Return_Request() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        RequestDto requestDto = requestService.addRequest(request, 1);
        assertEquals(1, requestDto.getId());
    }

    @Test
    void getRequestsList_Should_Return_RequestList() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(requestRepository.findByRequesterId(Mockito.anyInt())).thenReturn(requestList);
        Mockito.when(itemRepository.findByRequestId(Mockito.anyInt())).thenReturn(itemList);
        List<RequestDto> requestDtoList = requestService.getRequestsList(1);
        assertNotNull(requestDtoList);
        assertEquals(3, requestDtoList.size());
    }

    @Test
    void getRequestById_Should_Return_Request_By_Id() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(requestRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(request));
        Mockito.when(itemRepository.findByRequestId(Mockito.anyInt())).thenReturn(itemList);
        RequestDto requestDto = requestService.getRequestById(1, 1);
        assertNotNull(requestDto);
        assertEquals(1, requestDto.getId());
    }

    @Test
    void getAllRequests_Should_Return_All_Requests() {
        Page<Request> page = PageableExecutionUtils.getPage(requestList, PageRequest.of(0, 3),
                () -> requestList.size());
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findByRequestId(Mockito.anyInt())).thenReturn(itemList);
        Mockito.when(requestRepository.findAllRequests(Mockito.anyInt(), Mockito.any(Pageable.class)))
                .thenReturn(page);
        List<RequestDto> requestDtoList = requestService.getAllRequests(1, Optional.of(0),
                Optional.of(3));
        assertEquals(3, requestDtoList.size());
    }
}