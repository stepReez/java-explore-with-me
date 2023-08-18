package ru.practicum.service;

import ru.practicum.dto.UserDto;
import ru.practicum.dto.request.NewUserRequest;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto createUser(NewUserRequest user);

    void deleteUser(long userId);
}
