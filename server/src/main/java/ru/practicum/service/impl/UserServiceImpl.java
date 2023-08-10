package ru.practicum.service.impl;

import org.springframework.stereotype.Service;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.request.NewUserRequest;
import ru.practicum.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        return null;
    }

    @Override
    public UserDto createUser(NewUserRequest user) {
        return null;
    }

    @Override
    public void deleteUser(long userId) {

    }
}
