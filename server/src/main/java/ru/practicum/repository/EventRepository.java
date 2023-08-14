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
            "WHERE e.annotation LIKE ?1 " +
            "OR e.title LIKE ?2 " +
            "AND c.id IN ?3 " +
            "AND e.paid LIKE ?4 " +
            "AND e.eventDate > ?5 " +
            "AND e.eventDate < ?6 " +
            "ORDER BY e.eventDate")
    List<Event> findPublicSortByDate(String text, String text2, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                           LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "WHERE e.annotation LIKE ?1 " +
            "OR e.title LIKE ?2 " +
            "AND c.id IN ?3 " +
            "AND e.paid LIKE ?4 " +
            "AND e.eventDate > ?5 " +
            "AND e.eventDate < ?6 " +
            "ORDER BY e.views DESC")
    List<Event> findPublicSortByViews(String text, String text2, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "JOIN e.initiator AS u " +
            "WHERE u.id IN ?1 " +
            "AND e.state IN ?2 " +
            "AND c.id IN ?3 " +
            "AND e.eventDate > ?4 " +
            "AND e.eventDate < ?5")
    List<Event> findAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                          LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
