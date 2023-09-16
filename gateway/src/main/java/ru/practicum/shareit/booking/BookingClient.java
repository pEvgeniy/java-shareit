package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> create(BookingRequestDto bookingRequestDto, int userId) {
        return post("", userId, bookingRequestDto);
    }

    public ResponseEntity<Object> findAllByBooker(String state, int userId, int from, int size) {
        return get("?state={state}&from={from}&size={size}", userId,
                Map.of(
                        "state", state,
                        "from", from,
                        "size", size
                ));
    }

    public ResponseEntity<Object> findAllByOwner(String state, int userId, int from, int size) {
        return get("/owner?state={state}&from={from}&size={size}", userId,
                Map.of(
                        "state", state,
                        "from", from,
                        "size", size
                ));
    }

    public ResponseEntity<Object> findById(int id, int userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> update(Boolean approved, int id, int userId) {
        return patch("/" + id + "?approved={approved}", userId, Map.of("approved", approved), null);
    }

    public ResponseEntity<Object> deleteById(int id) {
        return delete("/" + id);
    }

}
