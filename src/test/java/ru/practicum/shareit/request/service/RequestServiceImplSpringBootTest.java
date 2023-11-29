package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(properties = {"db.name=shareit_test"}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RequestServiceImplSpringBootTest {

    private final EntityManager entityManager;
    private final RequestService requestService;
    private final UserService userService;

    private Request request = new Request();
    private Request request2 = new Request();
    private Request request3 = new Request();
    private User user;
    private User user2;
    private User user3;
    @Autowired
    private RequestMapper requestMapper;
    List<Request> requestList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        request.setDescription("описание").setCreated(LocalDateTime.now());
        request2.setDescription("описание2").setCreated(LocalDateTime.now());
        request3.setDescription("описание3").setCreated(LocalDateTime.now());
        Collections.addAll(requestList, request, request2, request3);
        user = new User().setEmail("serg@mail.ru").setName("Sergey");
        user2 = new User().setEmail("galina@mail.ru").setName("Galina");
        user3 = new User().setEmail("nick@mail.ru").setName("Nick");
        userService.addUser(user);
        userService.addUser(user2);
        userService.addUser(user3);
        requestService.addRequest(request, 1);
        requestService.addRequest(request2, 2);
        requestService.addRequest(request3, 1);
    }

    @Test
    void addRequest() {
        Request request4 = new Request().setDescription("описание").setCreated(LocalDateTime.now());
        requestService.addRequest(request4, 3);
        TypedQuery<Request> query = entityManager.createQuery("FROM Request WHERE id = :id", Request.class);
        Request dbRequest = query.setParameter("id", request4.getId()).getSingleResult();
        assertThat(dbRequest, equalTo(request4));
    }

    @Test
    void getRequestsList() {
        List<RequestDto> methodRequestList = requestService.getRequestsList(1);
        Query query = entityManager.createQuery("FROM Request WHERE requesterId = 1");
        List<Request> dbRequestList = query.getResultList();
        assertThat(dbRequestList.size(), equalTo(methodRequestList.size()));
        assertThat(dbRequestList.get(0).getId(), equalTo(methodRequestList.get(0).getId()));
    }

    @Test
    void getRequestById() {
        RequestDto methodRequestList = requestService.getRequestById(1, 1);
        TypedQuery<Request> query = entityManager.createQuery("FROM Request WHERE id = 1", Request.class);
        RequestDto dbRequest = requestMapper.toRequestDto(query.getSingleResult());
        assertThat(dbRequest.getId(), equalTo(methodRequestList.getId()));
        assertThat(dbRequest.getDescription(), equalTo(methodRequestList.getDescription()));
        assertThat(dbRequest.getCreated(), equalTo(methodRequestList.getCreated()));
    }

    @Test
    void getAllRequests() {
        Optional<Integer> from = Optional.of(0);
        Optional<Integer> size = Optional.of(2);
        List<RequestDto> methodRequest = requestService.getAllRequests(1, from, size);
        Query query = entityManager.createQuery("FROM Request WHERE requesterId != 1");
        List<RequestDto> dbRequestDtotListList = query.getResultList();
        System.out.println(methodRequest.size());
        assertThat(dbRequestDtotListList.size(), equalTo(methodRequest.size()));
    }
}