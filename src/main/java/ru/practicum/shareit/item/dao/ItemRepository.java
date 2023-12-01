package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByOwnerId(int ownerId);

    Page<Item> findByOwnerId(int ownerId, Pageable pageable);

    List<Item> findByRequestId(int requestId);

    List<Item> findByNameIgnoreCaseContaining(String request);

    @Query(value = "SELECT i FROM Item AS i WHERE LOWER(i.name) LIKE LOWER(concat('%', :request, '%')) " +
            "or LOWER(i.description) LIKE LOWER(concat('%', :request, '%'))")
    Page<Item> findByNameOrDescriptionWithPagination(@Param("request") String request, Pageable pageable);

    List<Item> findByDescriptionIgnoreCaseContaining(String request);

}
