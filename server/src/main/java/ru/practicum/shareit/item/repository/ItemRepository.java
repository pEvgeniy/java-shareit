package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByOwnerIdOrderByIdAsc(Integer ownerId, PageRequest pageRequest);

    List<Item> findByRequestId(Integer requestId);

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.available = true " +
            "AND (LOWER(i.name) LIKE %:text% OR LOWER(i.description) LIKE %:text%)")
    List<Item> searchAvailableItemsByText(@Param("text") String text, PageRequest pageRequest);
}
