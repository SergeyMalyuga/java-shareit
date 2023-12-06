package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;


import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.user.User;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBookerEquals(User user);

    List<Booking> findByItemEquals(Item item);

    @Query(value = "SELECT b FROM Booking AS b WHERE b.booker.id = :userId ORDER BY id DESC")
    Page<Booking> findByBookerIdWithPagination(@Param("userId") int userId, Pageable pageable);

    Page<Booking> findByItemOwnerId(int userId, Pageable pageable);


}

