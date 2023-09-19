package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer id;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime end;

    @OneToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @OneToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;
}
