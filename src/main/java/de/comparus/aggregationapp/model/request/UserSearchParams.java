package de.comparus.aggregationapp.model.request;


import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Getter
public class UserSearchParams {

    private final Map<String, String> queryParams;
    private final String dbName;

    public UserSearchParams(Map<String, String> queryParams) {
        this.queryParams = Optional.ofNullable(queryParams)
                .orElse(Collections.emptyMap());
        this.dbName = this.queryParams.get("dbName");
    }

}
