package de.comparus.aggregationapp.config;

import de.comparus.aggregationapp.model.DbProvider;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ConfigurationProperties
@RequiredArgsConstructor
@Data
public class CustomDataSourcesConfig {

    private final List<DataSourceProperties> dataSources;

    @Bean
    public Map<String, DataSourceWrapper> dataSourcesMap() {
        return dataSources.stream()
                .collect(Collectors.toMap(DataSourceProperties::getName, this::createDataSourceWrapper));
    }

    private DataSourceWrapper createDataSourceWrapper(DataSourceProperties config) {
        var dataSource = createDataSource(config);
        return new DataSourceWrapper(config, dataSource, new JdbcTemplate(dataSource));
    }

    private DataSource createDataSource(DataSourceProperties config) {
        return DataSourceBuilder.create()
                .driverClassName(config.getStrategy()
                        .getDriverClassName())
                .url(config.getUrl())
                .username(config.getUser())
                .password(config.getPassword())
                .build();
    }


    @Data
    public static class DataSourceProperties {
        @NotNull
        private String name;
        @NotNull
        private DbProvider strategy;
        @NotNull
        private String url;
        @NotNull
        private String table;
        @NotNull
        private String user;
        @NotNull
        private String password;
        @NotNull
        private Map<String, String> mapping;
    }

    public record DataSourceWrapper(DataSourceProperties properties, DataSource dataSource, JdbcTemplate jdbcTemplate) {
    }


}
