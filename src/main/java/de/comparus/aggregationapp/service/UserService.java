package de.comparus.aggregationapp.service;

import de.comparus.aggregationapp.data.UserRepository;
import de.comparus.aggregationapp.model.User;
import de.comparus.aggregationapp.model.request.UserSearchParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<User> getAllUsers(UserSearchParams searchParams) {
        return repository.getUsersBySearchParameters(searchParams);
    }
}
