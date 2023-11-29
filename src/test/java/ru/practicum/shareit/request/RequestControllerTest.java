package ru.practicum.shareit.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.LocalDateTimeAdapter;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {

    @MockBean
    private RequestService requestService;
    @Autowired
    private MockMvc mvc;
    private Request request;
    private User user;
    private RequestDto requestDto;
    private RequestDto requestDto2;
    private RequestDto requestDto3;
    private List<RequestDto> requestDtoList = new ArrayList<>();
    private Gson gson;

    @BeforeEach
    private void setUp() {
        gson = new GsonBuilder().serializeNulls().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        user = new User().setId(1).setEmail("serg@mail.ru").setName("Sergey");
        request = new Request().setId(1).setDescription("description").setCreated(LocalDateTime.now());
        requestDto = new RequestDto().setId(1).setDescription("description")
                .setCreated(LocalDateTime.now());
        requestDto2 = new RequestDto().setId(2).setDescription("description2")
                .setCreated(LocalDateTime.now()).setItems(new ArrayList<>());
        requestDto3 = new RequestDto().setId(3).setDescription("description3")
                .setCreated(LocalDateTime.now()).setItems(new ArrayList<>());
        Collections.addAll(requestDtoList, requestDto, requestDto2, requestDto3);
    }

    @Test
    void addRequest_Should_Return_Request() throws Exception {
        when(requestService.addRequest(Mockito.any(Request.class), Mockito.anyInt())).thenReturn(requestDto);
        requestService.addRequest(request, 1);
        Map<String, String> request = new HashMap<>();
        request.put("description", "Хотел бы воспользоваться щёткой для обуви");
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(gson.toJson(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId())))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
    }

    @Test
    void getRequestsList_Should_Return_RequestList() throws Exception {
        when(requestService.getRequestsList(Mockito.anyInt())).thenReturn(requestDtoList);
        requestService.getRequestsList(1);

        mvc.perform(get("/requests").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

        String json = gson.toJson(requestDtoList);
        Configuration conf = Configuration.defaultConfiguration();
        String description = JsonPath.using(conf).parse(json).read("$[0]['description']");
        String description1 = JsonPath.using(conf).parse(json).read("$[1]['description']");
        String description2 = JsonPath.using(conf).parse(json).read("$[2]['description']");
        assertEquals(requestDto.getDescription(), description);
        assertEquals(requestDto2.getDescription(), description1);
        assertEquals(requestDto3.getDescription(), description2);
    }

    @Test
    void getRequestById_Should_Return_Request_By_Id() throws Exception {
        when(requestService.getRequestById(Mockito.anyInt(), Mockito.anyInt())).thenReturn(requestDto2);
        requestService.getRequestById(1, 1);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(gson.toJson(requestDto2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto2.getId())))
                .andExpect(jsonPath("$.description", is(requestDto2.getDescription())));
    }

    @Test
    void getAllRequests_Should_Return_All_Requests() throws Exception {
        when(requestService.getAllRequests(Mockito.anyInt(), Mockito.any(Optional.class), Mockito.any(Optional.class)))
                .thenReturn(requestDtoList);
        Optional<Integer> from = Optional.of(0);
        Optional<Integer> size = Optional.of(0);

        requestService.getAllRequests(1, from, size);
        mvc.perform(get("/requests/all").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }
}