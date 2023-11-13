package de.comparus.aggregationapp.data;

import de.comparus.aggregationapp.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();
}
