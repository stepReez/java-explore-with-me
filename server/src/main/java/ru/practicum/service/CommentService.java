package ru.practicum.service;

import ru.practicum.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto, long userId, long eventId);

    CommentDto patchComment(CommentDto commentDto, long userId, long comId);

    List<CommentDto> getCommentsByUser(long userId);

    void deleteCommentByUser(long userId, long comId);

    CommentDto getCommentById(long userId, long comId);

    void deleteCommentByAdmin(long comId);
}
