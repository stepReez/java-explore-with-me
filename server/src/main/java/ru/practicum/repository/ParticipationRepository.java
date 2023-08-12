package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.ParticipationRequest;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {
}
