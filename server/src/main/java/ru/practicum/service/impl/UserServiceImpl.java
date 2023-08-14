package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.request.NewUserRequest;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;
import ru.practicum.util.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        List<UserDto> users = userRepository.findAll(PageRequest.of(from / size, size)).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("Users from {} to {} found", from, from + size);
        return users;
    }

    @Override
    @Transactional
    public UserDto createUser(NewUserRequest user) {
        User userDto = userRepository.save(UserMapper.toUser(user));
        log.info("User with id = {} saved", userDto.getId());
        return UserMapper.toUserDto(userDto);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
        log.info("User with id = {} deleted", userId);
    }
}
