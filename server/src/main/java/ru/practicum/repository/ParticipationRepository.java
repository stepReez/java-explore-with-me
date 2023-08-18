package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query("SELECT r FROM ParticipationRequest AS r " +
            "JOIN r.requester AS u " +
            "WHERE u.id = ?1")
    List<ParticipationRequest> findAllByUserId(Long userId);

    @Query("SELECT r FROM ParticipationRequest AS r " +
            "JOIN r.requester AS u " +
            "JOIN r.event AS e " +
            "WHERE u.id = ?1 " +
            "AND e.id = ?2")
    Optional<ParticipationRequest> findAllByUserIdAndEventId(Long userId, Long eventId);

    @Query("SELECT r FROM ParticipationRequest AS r " +
            "JOIN r.event AS e " +
            "WHERE e.id = ?1")
    List<ParticipationRequest> findAllByEventId(Long eventId);

    @Query("SELECT r FROM ParticipationRequest AS r " +
            "JOIN r.event AS e " +
            "WHERE e.id = ?1")
    List<ParticipationRequest> findAllByUsersEvent(Long eventId);
}
