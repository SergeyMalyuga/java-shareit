package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByRequesterId(int requesterId);

    @Query(value = "SELECT * FROM requests WHERE requester_id != ?1 ORDER BY created DESC", nativeQuery = true)
    Page<Request> findAllRequests(int userId, Pageable pageable);
}
