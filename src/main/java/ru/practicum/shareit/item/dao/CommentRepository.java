package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value = "select * " +
            "from comments " +
            "where author = ?1 AND item_id = ?2", nativeQuery = true)
    Comment getUserCommentByItem(int userId, int itemId);

    @Query(value = "select * " +
            "from comments " +
            "where item_id = ?1", nativeQuery = true)
    List<Comment> getCommentByItem(int itemId);
}
