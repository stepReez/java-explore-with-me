package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;
import ru.practicum.util.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.initiator AS u " +
            "WHERE u.id = ?1")
    List<Event> findByUser(Long userId, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "WHERE ((:text) IS NULL OR lower(e.annotation) LIKE lower(concat('%', :text, '%')) " +
            "OR lower(e.title) LIKE lower(concat('%', :text, '%'))) " +
            "AND (:categories IS NULL OR c.id IN :categories) " +
            "AND e.paid = :paid " +
            "AND e.eventDate > :rangeStart " +
            "AND (cast(:rangeEnd as date) IS NULL OR e.eventDate < :rangeEnd) " +
            "AND e.state = 'PUBLISHED' " +
            "ORDER BY e.eventDate")
    List<Event> findPublicSortByDate(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                           LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "WHERE ((:text) IS NULL OR lower(e.annotation) LIKE lower(concat('%', :text, '%')) " +
            "OR lower(e.title) LIKE lower(concat('%', :text, '%'))) " +
            "AND (:categories IS NULL OR c.id IN :categories) " +
            "AND e.paid = :paid " +
            "AND e.eventDate > :rangeStart " +
            "AND (cast(:rangeEnd as date) IS NULL OR e.eventDate < :rangeEnd) " +
            "AND e.state = 'PUBLISHED' " +
            "ORDER BY e.views DESC")
    List<Event> findPublicSortByViews(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "JOIN e.initiator AS u " +
            "WHERE ((:users) IS NULL OR u.id IN :users) " +
            "AND ((:states) IS NULL OR e.state IN :states) " +
            "AND ((:categories) IS NULL OR c.id IN :categories) " +
            "AND e.eventDate > :rangeStart " +
            "AND (cast(:rangeEnd as date) IS NULL OR e.eventDate < :rangeEnd)")
    List<Event> findAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                          LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "WHERE c.id = ?1")
    List<Event> findByCategory(Long categoryId, Pageable pageable);
}
