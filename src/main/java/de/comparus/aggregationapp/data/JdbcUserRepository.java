package de.comparus.aggregationapp.data;

import de.comparus.aggregationapp.config.CustomDataSourcesConfig;
import de.comparus.aggregationapp.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final Map<String, CustomDataSourcesConfig.DataSourceWrapper> dataSourcesMap;

    public List<User> getAllUsers() {
        var wrappers = dataSourcesMap.values();
        return wrappers.parallelStream()
                .map(this::fetchUsersFromDataSource)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<User> fetchUsersFromDataSource(CustomDataSourcesConfig.DataSourceWrapper dataSourceWrapper) {
        return dataSourceWrapper.jdbcTemplate()
                .query("SELECT * FROM " + dataSourceWrapper.properties()
                        .getTable(), userRowMapper(dataSourceWrapper.properties()));
    }

    private RowMapper<User> userRowMapper(CustomDataSourcesConfig.DataSourceProperties properties) {
        var mapping = properties.getMapping();
        return (resultSet, rowNum) -> new User(resultSet.getString(mapping.get("id")), resultSet.getString(mapping.get("username")), resultSet.getString(mapping.get("name")), resultSet.getString(mapping.get("surname")));
    }

}
