package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CommentDto;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.CommentService;
import ru.practicum.util.EventState;
import ru.practicum.util.mapper.CommentMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    public CommentDto createComment(CommentDto commentDto, long userId, long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new BadRequestException("Event must be published");
        }
        Comment comment = CommentMapper.toComment(
                commentDto,
                userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found")),
                event
        );
        comment.setCreated(LocalDateTime.now());
        CommentDto commentDtoRet = CommentMapper.toCommentDto(commentRepository.save(comment));
        log.info("Comment with id = {} saved", commentDtoRet.getId());
        return commentDtoRet;
    }

    @Override
    public CommentDto patchComment(CommentDto commentDto, long userId, long comId) {
        Comment comment = checkSender(userId, comId);
        comment.setMessage(commentDto.getMessage());
        CommentDto commentDtoRet = CommentMapper.toCommentDto(commentRepository.save(comment));
        log.info("Comment with id = {} patched", commentDtoRet.getId());
        return commentDtoRet;
    }

    @Override
    public List<CommentDto> getCommentsByUser(long userId) {
        List<CommentDto> commentDtoList = commentRepository.getCommentsByUserId(userId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        log.info("Comments for user {} found", userId);
        return commentDtoList;
    }

    @Override
    public void deleteCommentByUser(long userId, long comId) {
        checkSender(userId, comId);
        commentRepository.deleteById(comId);
        log.info("Comment with id = {} deleted", comId);
    }

    @Override
    public CommentDto getCommentById(long userId, long comId) {
        CommentDto commentDto = CommentMapper.toCommentDto(commentRepository.findById(comId).orElseThrow(() ->
                new NotFoundException("Comment not found")));
        log.info("Comment with id = {} found", comId);
        return commentDto;
    }

    @Override
    public void deleteCommentByAdmin(long comId) {
        commentRepository.findById(comId).orElseThrow(() -> new NotFoundException("Comment not found"));
        commentRepository.deleteById(comId);
        log.info("Comment with id = {} deleted by admin", comId);
    }

    private Comment checkSender(long userId, long comId) {
        Comment comment = commentRepository.findById(comId).orElseThrow(() ->
                new NotFoundException("Comment not found"));
        if (comment.getSender().getId() != userId) {
            throw new BadRequestException("Can't delete other user's comments");
        }
        return comment;
    }
}
