package de.comparus.aggregationapp.rest;

import de.comparus.aggregationapp.model.User;
import de.comparus.aggregationapp.model.request.UserSearchParams;
import de.comparus.aggregationapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "User aggregation controller")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get a list of users", description = "Returns a list of users from all data sources")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "Fetched users")
    @ApiResponse(responseCode = "404", description = "Data base cannot be found by dbName")
    public ResponseEntity<List<User>> getAllUsers(
            @Parameter(
                    name = "queryParams",
                    description = "A map of query parameters where key can be 'name', 'username', 'surname', or 'dbName'.",
                    example = "{\"username\":\"john_doe\",\"dbName\":\"some-db-name\"}"
            )
            @RequestParam Map<String, String> queryParams) {
        var searchParams = new UserSearchParams(queryParams);
        return ResponseEntity.ok(userService.getAllUsers(searchParams));
    }
}
