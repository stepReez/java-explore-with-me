package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CommentDto;
import ru.practicum.service.impl.CommentServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentPrivateController {
    private final CommentServiceImpl commentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@Valid CommentDto commentDto,
                                    @PathVariable long userId, @PathVariable long eventId) {
        return commentService.createComment(commentDto, userId, eventId);
    }

    @PatchMapping("/{comId}")
    public CommentDto patchComment(@Valid CommentDto commentDto, @PathVariable long userId, @PathVariable long comId) {
        return commentService.patchComment(commentDto, userId, comId);
    }

    @GetMapping
    public List<CommentDto> getCommentsByUser(@PathVariable long userId) {
        return commentService.getCommentsByUser(userId);
    }

    @GetMapping("/{comId}")
    public CommentDto getCommentById(@PathVariable long userId, @PathVariable long comId) {
        return commentService.getCommentById(userId, comId);
    }

    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByUser(@Valid CommentDto commentDto,
                                    @PathVariable int userId, @PathVariable int comId) {
        commentService.deleteCommentByUser(commentDto, userId, comId);
    }
}
