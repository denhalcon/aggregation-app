package de.comparus.aggregationapp.rest;

import de.comparus.aggregationapp.model.User;
import de.comparus.aggregationapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
//        log.info("available data sources: {}" , configuration.getDataSources());
        return userService.getAllUsers();
    }
}
