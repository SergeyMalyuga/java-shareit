package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestGatewayController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> postRequest(@Valid @RequestBody RequestDto request,
                                              @RequestHeader("X-Sharer-User-Id") int userId) {
        return requestClient.postRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsList(@RequestHeader("X-Sharer-User-Id") int userId) {
        return requestClient.getRequestsList(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") int userId,
                                                 @PathVariable(name = "requestId") int requestId) {
        return requestClient.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsList(@RequestHeader("X-Sharer-User-Id") int userId,
                                                     @PositiveOrZero @RequestParam(name = "from",
                                                             defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size",
                                                             defaultValue = "10") Integer size) {
        return requestClient.getAllRequestsList(userId, from, size);
    }
}
