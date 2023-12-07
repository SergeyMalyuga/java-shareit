package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    Page<Request> findByRequesterId(int requesterId, Pageable pageable);

    List<Request> findByRequesterId(int requesterId);

    @Query(value = "SELECT r FROM Request AS r WHERE r.requesterId != :userId ORDER BY created DESC")
    Page<Request> findAllRequests(@Param("userId") int userId, Pageable pageable);
}
