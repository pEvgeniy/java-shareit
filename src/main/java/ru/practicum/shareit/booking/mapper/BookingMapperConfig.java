package ru.practicum.shareit.booking.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookingMapperConfig {

    @Bean
    public BookingMapper bookingMapper() {
        return new BookingMapper();
    }
}
