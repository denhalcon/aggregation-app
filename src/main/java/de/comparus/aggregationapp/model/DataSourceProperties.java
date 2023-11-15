package de.comparus.aggregationapp.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
public class DataSourceProperties {
    @NotNull
    private String name;
    @NotNull
    private DbProvider strategy;
    @NotNull
    private String url;
    @NotNull
    private String table;
    @NotNull
    @ToString.Exclude
    private String user;
    @NotNull
    @ToString.Exclude
    private String password;
    @NotNull
    private Map<String, String> mapping;
}