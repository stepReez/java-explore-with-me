package ru.practicum.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.request.NewUserRequest;
import ru.practicum.service.UserService;

import java.util.List;

@Controller
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(defaultValue = "") List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    public UserDto createUser(@RequestBody NewUserRequest user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/userId")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
