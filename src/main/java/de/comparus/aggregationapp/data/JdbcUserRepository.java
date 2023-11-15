package de.comparus.aggregationapp.data;

import de.comparus.aggregationapp.model.DataSourceInfo;
import de.comparus.aggregationapp.model.DataSourceProperties;
import de.comparus.aggregationapp.model.User;
import de.comparus.aggregationapp.model.request.UserSearchParams;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final Map<String, DataSourceInfo> dataSourceInfoMap;

    @Override
    public List<User> getUsersBySearchParameters(UserSearchParams searchParams) {
        if (StringUtils.isNotBlank(searchParams.getDbName())) {
            var dataSourceInfo = Optional.ofNullable(dataSourceInfoMap.get(searchParams.getDbName()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data source not found"));

            return fetchUsers(dataSourceInfo, searchParams);
        }

        return dataSourceInfoMap.values()
                .parallelStream()
                .map(dataSourceInfo -> fetchUsers(dataSourceInfo, searchParams))
                .flatMap(Collection::stream)
                .toList();
    }

    private List<User> fetchUsers(DataSourceInfo dataSourceInfo, UserSearchParams searchParams) {
        String sql = createQueryWithFilters(dataSourceInfo.properties(), searchParams.getQueryParams());
        return dataSourceInfo.jdbcOperations()
                .query(sql, new MapSqlParameterSource(searchParams.getQueryParams()),
                        userRowMapper(dataSourceInfo.properties()));
    }

    private String createQueryWithFilters(DataSourceProperties properties, Map<String, String> queryParams) {
        String baseQuery = "SELECT * FROM " + properties.getTable() + " WHERE 1=1";
        StringBuilder queryBuilder = new StringBuilder(baseQuery);

        Map<String, String> columnMappings = properties.getMapping();

        queryParams.forEach((key, value) -> {
            String columnName = columnMappings.get(key);
            if (columnName != null && StringUtils.isNotBlank(value)) {
                queryBuilder.append(" AND ")
                        .append(columnName)
                        .append(" = :")
                        .append(key);
            }
        });
        return queryBuilder.toString();
    }

    private RowMapper<User> userRowMapper(DataSourceProperties properties) {
        var mapping = properties.getMapping();
        return (resultSet, rowNum) -> new User(resultSet.getString(mapping.get("id")),
                resultSet.getString(mapping.get("username")), resultSet.getString(mapping.get("name")),
                resultSet.getString(mapping.get("surname")));
    }

}
