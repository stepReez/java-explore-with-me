package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.impl.CommentServiceImpl;

@RestController
@RequestMapping("admin/comments")
@RequiredArgsConstructor
public class CommentAdminController {
    private final CommentServiceImpl commentService;

    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable long comId) {
        commentService.deleteCommentByAdmin(comId);
    }
}
