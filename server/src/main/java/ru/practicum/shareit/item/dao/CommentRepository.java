package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value = "select c " +
            "from Comment AS c " +
            "where c.author.id = :authorId AND c.item.id = :itemId")
    Comment getUserCommentByItem(@Param("authorId") int userId, @Param("itemId") int itemId);

    @Query(value = "select c " +
            "from Comment AS c " +
            "where c.item.id = :itemId")
    List<Comment> getCommentByItem(@Param("itemId") int itemId);
}
