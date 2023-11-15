package de.comparus.aggregationapp.data;

import de.comparus.aggregationapp.model.User;
import de.comparus.aggregationapp.model.request.UserSearchParams;

import java.util.List;

public interface UserRepository {
    List<User> getUsersBySearchParameters(UserSearchParams searchParams);
}
