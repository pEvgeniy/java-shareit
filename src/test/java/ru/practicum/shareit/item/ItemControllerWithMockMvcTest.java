package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentShortDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.ItemController.X_SHARER_USER_ID;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerWithMockMvcTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(1)
                .name("item name")
                .description("item description")
                .available(true)
                .requestId(null)
                .comments(Collections.emptyList())
                .build();
    }

    @Test
    void createItem() throws Exception {
        Mockito
                .when(itemService.createItem(any(ItemDto.class), any(Integer.class)))
                .thenReturn(itemDto);
        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));
    }

    @Test
    void findAll() throws Exception {
        Mockito
                .when(itemService.findAll(any(Integer.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(itemDto));
        mockMvc.perform(get("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDto))));
    }

    @Test
    void findById() throws Exception {
        Mockito
                .when(itemService.findById(any(Integer.class), any(Integer.class)))
                .thenReturn(itemDto);
        mockMvc.perform(get("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));
    }

    @Test
    void update() throws Exception {
        Mockito
                .when(itemService.update(any(Integer.class), any(ItemDto.class), any(Integer.class)))
                .thenReturn(itemDto);
        mockMvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));
    }

    @Test
    void deleteById() throws Exception {
        Mockito
                .when(itemService.deleteById(1))
                .thenReturn(1);
        mockMvc.perform(delete("/items/1")
                        .content(mapper.writeValueAsString(1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(1)));
    }

    @Test
    void search() throws Exception {
        Mockito
                .when(itemService.search(any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(itemDto));
        mockMvc.perform(get("/items/search?text=тЕкСт")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDto))));
    }

    @Test
    void createComment() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("comment text")
                .authorName("author name")
                .created(LocalDateTime.now())
                .build();
        CommentShortDto commentShortDto = CommentShortDto.builder()
                .text("comment text")
                .build();
        Mockito
                .when(itemService.createComment(any(CommentShortDto.class), any(Integer.class), any(Integer.class)))
                .thenReturn(commentDto);
        mockMvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentShortDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDto)));
    }
}