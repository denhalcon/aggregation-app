package de.comparus.aggregationapp.model;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

public record DataSourceInfo(DataSourceProperties properties, NamedParameterJdbcOperations jdbcOperations) { }
