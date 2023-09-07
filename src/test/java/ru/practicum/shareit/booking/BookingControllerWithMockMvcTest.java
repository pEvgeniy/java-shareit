package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerWithMockMvcTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private BookingDto bookingDto;

    private BookingShortDto bookingShortDto;

    @BeforeEach
    void setUp() {
        User owner = User.builder()
                .id(1)
                .name("owner name")
                .email("owner@mail.com")
                .build();
        User booker = User.builder()
                .id(1)
                .name("booker name")
                .email("booker@mail.com")
                .build();
        Item item = Item.builder()
                .id(1)
                .name("item name")
                .description("item description")
                .available(true)
                .owner(owner)
                .build();
        bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
        bookingShortDto = BookingShortDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(2))
                .build();
    }

    @Test
    void create() throws Exception {
        Mockito
                .when(bookingService.create(any(BookingShortDto.class), any(Integer.class)))
                .thenReturn(bookingDto);
        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingShortDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void findAllByBooker() throws Exception {
        Mockito
                .when(bookingService.findAllByBooker(
                        any(String.class), any(Integer.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(bookingDto));
        mockMvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDto))));
    }

    @Test
    void findAllByOwner() throws Exception {
        Mockito
                .when(bookingService.findAllByOwner(
                        any(String.class), any(Integer.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(bookingDto));
        mockMvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDto))));
    }

    @Test
    void findById() throws Exception {
        Mockito
                .when(bookingService.findById(any(Integer.class), any(Integer.class)))
                .thenReturn(bookingDto);
        mockMvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void update() throws Exception {
        Mockito
                .when(bookingService.update(any(Boolean.class), any(Integer.class), any(Integer.class)))
                .thenReturn(bookingDto);
        mockMvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingShortDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void delete() throws Exception {
        Mockito
                .when(bookingService.deleteById(any(Integer.class)))
                .thenReturn(1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(1)));
    }
}