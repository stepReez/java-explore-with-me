package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment AS c " +
            "JOIN c.sender AS u " +
            "WHERE u.id = ?1")
    List<Comment> getCommentsByUserId(Long userId);

    @Query("SELECT c FROM Comment AS c " +
            "JOIN c.event AS e " +
            "WHERE e.id = ?1")
    List<Comment> getCommentsByEventId(Long eventId);
}
