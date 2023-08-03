package ru.practicum.statService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.statService.model.Hit;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

}
