package ru.practicum.util.mapper;

import ru.practicum.dto.CommentDto;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.util.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .created(comment.getCreated().format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT)))
                .message(comment.getMessage())
                .senderId(comment.getSender().getId())
                .eventId(comment.getEvent().getId())
                .build();
    }

    public static Comment toComment(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .id(commentDto.getId())
                .message(commentDto.getMessage())
                .sender(user)
                .event(event)
                .build();
    }
}
