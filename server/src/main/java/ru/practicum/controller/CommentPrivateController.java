package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.CommentDto;
import ru.practicum.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                                    @PathVariable long userId, @PathVariable long eventId) {
        return commentService.createComment(commentDto, userId, eventId);
    }

    @PatchMapping("/{comId}")
    public CommentDto patchComment(@Valid @RequestBody CommentDto commentDto,
                                   @PathVariable long userId, @PathVariable long comId) {
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
    public void deleteCommentByUser(@PathVariable int userId, @PathVariable int comId) {
        commentService.deleteCommentByUser(userId, comId);
    }
}
