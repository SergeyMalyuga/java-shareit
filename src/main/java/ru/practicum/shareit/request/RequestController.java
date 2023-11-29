package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    @Autowired
    RequestService requestService;

    @PostMapping()
    public RequestDto postRequest(@Valid @RequestBody Request request, @RequestHeader("X-Sharer-User-Id") int userId) {
        return requestService.addRequest(request, userId);
    }

    @GetMapping()
    public List<RequestDto> getRequestsList(@RequestHeader("X-Sharer-User-Id") int userId) {
        return requestService.getRequestsList(userId);
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") int userId
            , @PathVariable(name = "requestId") int requestId) {
        return requestService.getRequestById(requestId, userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAllRequestsList(@RequestHeader("X-Sharer-User-Id") int userId
            , @RequestParam(name = "from", required = false) Optional<Integer> from
            , @RequestParam(name = "size", required = false) Optional<Integer>  size) {
        return requestService.getAllRequests(userId, from, size);
    }
}
