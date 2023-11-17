package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "text")
    private String text;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private User author;
    @Column(name = "created")
    private LocalDateTime created;
    @Transient
    String authorName;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (this.getClass() != object.getClass()) return false;
        Comment comment = (Comment) object;
        return this.id == comment.id && Objects.equals(this.text, comment.text)
                && Objects.equals(this.item, comment.item)
                && Objects.equals(this.author, comment.author)
                && Objects.equals(this.created, comment.created)
                && Objects.equals(this.authorName, comment.authorName);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (id != 0) {
            hash = hash + id;
        }
        hash = hash * 31;
        if (text != null) {
            hash = hash + text.hashCode();
        }
        hash = hash * 31;
        if (item != null) {
            hash = hash + item.hashCode();
        }
        hash = hash * 31;
        if (author != null) {
            hash = hash + author.hashCode();
        }
        hash = hash * 31;
        if (created != null) {
            hash = hash + created.hashCode();
        }
        hash = hash * 31;
        if (authorName != null) {
            hash = hash + authorName.hashCode();
        }
        return hash;
    }
}
