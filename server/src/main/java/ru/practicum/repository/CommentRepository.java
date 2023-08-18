package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comments AS c " +
            "JOIN c.sender AS u " +
            "WHERE u.id = :userId")
    List<Comment> findCommentsByUser(long userId);

    @Query("SELEVT c FROM Comments AS c " +
            "JOIN c.event AS e " +
            "WHERE e.id = :eventId")
    List<Comment> findCommentsByEvent(long eventId);
}
