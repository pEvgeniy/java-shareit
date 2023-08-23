package ru.practicum.shareit.item.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommentMapperConfig {

    @Bean
    public CommentMapper commentMapper() {
        return new CommentMapper();
    }

}
