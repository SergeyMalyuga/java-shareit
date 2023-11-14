package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByOwnerIdEquals(int ownerId);

    List<Item> findByNameIgnoreCaseContaining(String request);

    List<Item> findByDescriptionIgnoreCaseContaining(String request);
}
