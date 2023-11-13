package de.comparus.aggregationapp.rest;

import de.comparus.aggregationapp.model.User;
import de.comparus.aggregationapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User", description = "The User API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get a list of users", description = "Returns a list of users from all data sources")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
