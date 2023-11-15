package de.comparus.aggregationapp.rest;

import de.comparus.aggregationapp.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.util.CollectionUtils.toMultiValueMap;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureWebTestClient(timeout = "PT3S")
class UserControllerTest {

    private static final String BASE_PATH = "/users";

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13.3")
            .withDatabaseName("agg_app_db")
            .withUsername("test_user")
            .withPassword("test_pass")
            .withInitScript("db/postgres-init.sql");

    @Container
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.2.0")
            .withDatabaseName("user_db")
            .withUsername("user")
            .withPassword("password")
            .withInitScript("db/mysql-init.sql");


    @Autowired
    private WebTestClient client;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("TEST_DB_1_URL", postgresContainer.getJdbcUrl());
        System.setProperty("TEST_DB_2_URL", mySQLContainer.getJdbcUrl());
    }


    @Test
    public void getUsers_whenCalledWithoutParams_shouldReturnUsersFromAllDataSources() {
        client.get()
                .uri(BASE_PATH)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<User>>() {
                })
                .value(result -> assertThat(result).hasSize(8));

    }

    @ParameterizedTest
    @MethodSource("argsForUserFilteringByFields")
    void getUsers_whenCalledWithParams_shouldFilterUsersByFields(Map<String, List<String>> params, int expectedUserCount) {
        client.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_PATH)
                        .queryParams(toMultiValueMap(params))
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<User>>() {
                })
                .value(result -> {
                    assertNotNull(result);
                    assertThat(result).hasSize(expectedUserCount);
                });
    }

    private static Stream<Arguments> argsForUserFilteringByFields() {
        return Stream.of(
                //filtering by dbName
                Arguments.of(Map.of("dbName", List.of("test-db-1")), 4),
                Arguments.of(Map.of("dbName", List.of("test-db-1")), 4),
                //filtering by user properties without dbName
                Arguments.of(Map.of("surname", List.of("Doe")), 2),
                Arguments.of(Map.of("username", List.of("user1")), 1),
                Arguments.of(Map.of("username", List.of("skywalker")), 1),
                //searching by user properties and dbName
                Arguments.of(Map.of("dbName", List.of("test-db-2"), "surname", List.of("Doe")), 0),
                Arguments.of(Map.of("dbName", List.of("test-db-1"), "surname", List.of("Doe")), 2),
                Arguments.of(Map.of("dbName", List.of("test-db-1"), "username", List.of("skywalker")), 0),
                Arguments.of(Map.of("dbName", List.of("test-db-2"), "username", List.of("skywalker")), 1),
                //searching for user that appears in both dbs
                Arguments.of(Map.of("username", List.of("commonUser")), 2),
                Arguments.of(Map.of("dbName", List.of("test-db-1"), "username", List.of("commonUser")), 1),
                Arguments.of(Map.of("dbName", List.of("test-db-2"), "username", List.of("commonUser")), 1),
                //searching user by multiple fields
                Arguments.of(Map.of("dbName", List.of("test-db-1"),
                        "username", List.of("commonUser"), "surname", List.of("Potter")), 1),
                Arguments.of(Map.of("dbName", List.of("test-db-2"),
                        "username", List.of("commonUser"), "surname", List.of("Potter")), 0),
                Arguments.of(Map.of("dbName", List.of("test-db-2"),
                        "username", List.of("commonUser"), "surname", List.of("Potter2")), 1)
        );
    }

    @Test
    void getUsers_whenCalledWithInvalidDbName_shouldReturn404Response() {
        client.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_PATH)
                        .queryParam("dbName", "invalidDbName")
                        .build())
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
