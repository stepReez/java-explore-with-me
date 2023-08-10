package ru.practicum.controller.adminController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.request.NewUserRequest;

import java.util.List;

@Controller
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return null;
    }

    @PostMapping
    public UserDto createUser(@RequestBody NewUserRequest user) {
        return null;
    }

    @DeleteMapping("/userId")
    public void deleteUser(@PathVariable long userId) {

    }
}
