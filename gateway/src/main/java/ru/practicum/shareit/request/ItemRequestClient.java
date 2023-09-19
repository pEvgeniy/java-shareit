package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> create(RequestDto requestDto, int userId) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> findAllByRequester(int userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findAllFromOthers(int userId, int from, int size) {
        return get("/all", userId,
                Map.of(
                        "from", from,
                        "size", size
                ));
    }

    public ResponseEntity<Object> findById(int itemRequestId, int userId) {
        return get("/" + itemRequestId, userId);
    }
}
