package de.comparus.aggregationapp.config;

import de.comparus.aggregationapp.model.DataSourceInfo;
import de.comparus.aggregationapp.model.DataSourceProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

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
    public Map<String, DataSourceInfo> dataSourcesMap() {
        return dataSources.stream()
                .collect(Collectors.toMap(DataSourceProperties::getName, this::createDataSourceWrapper));
    }

    private DataSourceInfo createDataSourceWrapper(DataSourceProperties dataSourceProperties) {
        var dataSource = createDataSource(dataSourceProperties);
        return new DataSourceInfo(dataSourceProperties, new NamedParameterJdbcTemplate(dataSource));
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

}
