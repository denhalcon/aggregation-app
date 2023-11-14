package de.comparus.aggregationapp.service;

import de.comparus.aggregationapp.data.UserRepository;
import de.comparus.aggregationapp.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }
}
