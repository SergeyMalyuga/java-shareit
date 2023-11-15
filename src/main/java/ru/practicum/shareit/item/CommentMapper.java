package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.UserMapper;

@Component
public class CommentMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ItemMapper itemMapper;

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto().setId(comment.getId())
                .setItem(itemMapper.itemDto(comment.getItem()))
                .setAuthor(userMapper.toUserDto(comment.getAuthor()))
                .setText(comment.getText())
                .setCreated(comment.getCreated())
                .setAuthorName(comment.getAuthorName());
    }
}
