package de.comparus.aggregationapp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DbProvider {
    POSTGRES("org.postgresql.Driver"),
    MYSQL("com.mysql.cj.jdbc.Driver");

    private final String driverClassName;
}
