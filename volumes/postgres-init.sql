CREATE TABLE Users
(
    user_id    VARCHAR(255) PRIMARY KEY,
    login      VARCHAR(255),
    first_name VARCHAR(255),
    last_name  VARCHAR(255)
);

INSERT INTO Users (user_id, login, first_name, last_name)
VALUES ('1', 'user1', 'John', 'Doe'),
       ('2', 'user2', 'Jane', 'Doe'),
       ('3', 'user3', 'Jim', 'Beam');