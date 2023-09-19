package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestClientDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> create(ItemRequestClientDto itemRequestDto, int userId) {
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> findAll(int userId, int from, int size) {
        return get("?from={from}&size={size}", userId,
                Map.of(
                        "from", from,
                        "size", size
                ));
    }

    public ResponseEntity<Object> findById(int id, int userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> update(int id, int userId, ItemRequestClientDto itemRequestDto) {
        return patch("/" + id, userId, itemRequestDto);
    }

    public ResponseEntity<Object> deleteById(int id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> search(String text, int from, int size) {
        return get("/search?text={text}&from={from}&size={size}",
                Map.of(
                        "text", text,
                        "from", from,
                        "size", size
                ));
    }

    public ResponseEntity<Object> createComment(CommentRequestDto commentRequestDto, int itemId, int userId) {
        return post("/" + itemId + "/comment", userId, commentRequestDto);
    }

}
