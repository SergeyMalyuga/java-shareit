package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.UserDtoMapper;

@Component
@Getter
@Setter
public class CommentMapper {

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Autowired
    private ItemDtoMapper itemDtoMapper;

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto().setId(comment.getId())
                .setItem(itemDtoMapper.itemDto(comment.getItem()))
                .setAuthor(userDtoMapper.toUserDto(comment.getAuthor()))
                .setText(comment.getText())
                .setCreated(comment.getCreated())
                .setAuthorName(comment.getAuthorName());
    }
}
