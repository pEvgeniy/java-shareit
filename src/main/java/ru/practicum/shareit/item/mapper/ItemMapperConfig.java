package ru.practicum.shareit.item.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemMapperConfig {
    @Bean
    public ItemMapper itemMapper() {
        return new ItemMapper();
    }
}
