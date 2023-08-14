package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;
import ru.practicum.util.EventState;
import ru.practicum.util.EventsSort;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.initiator AS u " +
            "WHERE u.id = ?1")
    List<Event> findByUser(long userId, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "WHERE annotation ILIKE ?1 " +
            "OR title ILIKE ?2 " +
            "AND c.id IN ?3 " +
            "AND paid LIKE ?4 " +
            "AND eventDate > ?5 " +
            "AND eventDate < ?6 " +
            "ORDER BY eventDate")
    List<Event> findPublicSortByDate(String text, String text2, List<Long> categories, boolean paid, LocalDateTime rangeStart,
                           LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "WHERE annotation ILIKE ?1 " +
            "OR title ILIKE ?2 " +
            "AND c.id IN ?3 " +
            "AND paid LIKE ?4 " +
            "AND eventDate > ?5 " +
            "AND eventDate < ?6 " +
            "ORDER BY views DESC")
    List<Event> findPublicSortByViews(String text, String text2, List<Long> categories, boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "JOIN e.category AS c " +
            "JOIN e.initiator AS u " +
            "WHERE u.id IN ?1 " +
            "AND state IN ?2 " +
            "AND c.id IN ?3 " +
            "AND eventDate > ?4 " +
            "AND eventDate < ?5")
    List<Event> findAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                          LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
