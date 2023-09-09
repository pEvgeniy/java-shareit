package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.request.ItemRequestController.X_SHARER_USER_ID;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerWithMockMvcTest {

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("description")
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();
    }

    @Test
    void create() throws Exception {
        Mockito
                .when(itemRequestService.create(any(ItemRequestDto.class), any(Integer.class)))
                .thenReturn(itemRequestDto);
        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));
    }

    @Test
    void findAll() throws Exception {
        Mockito
                .when(itemRequestService.findAllByRequester(any(Integer.class)))
                .thenReturn(List.of(itemRequestDto));
        mockMvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDto))));
    }

    @Test
    void findAllFromOthers() throws Exception {
        Mockito
                .when(itemRequestService.findAllFromOthers(any(Integer.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(itemRequestDto));
        mockMvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDto))));
    }

    @Test
    void findById() throws Exception {
        Mockito
                .when(itemRequestService.findById(any(Integer.class), any(Integer.class)))
                .thenReturn(itemRequestDto);
        mockMvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));
    }
}