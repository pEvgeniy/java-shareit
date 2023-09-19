package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void findAllByItemId() {
        User user = User.builder()
                .name("user name")
                .email("user@mail.com")
                .build();
        userRepository.save(user);
        Item item = Item.builder()
                .name("item name")
                .description("description")
                .available(true)
                .owner(user)
                .build();
        itemRepository.save(item);
        Comment comment = Comment.builder()
                .text("my first comment")
                .created(LocalDateTime.now())
                .author(user)
                .item(item)
                .build();
        commentRepository.save(comment);

        List<Comment> foundComments = commentRepository.findAllByItemId(1);
        assertEquals(1, foundComments.size());
    }
}