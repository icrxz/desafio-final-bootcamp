package com.mercadolibre.frescos_api_grupo_2_w2.config;

import com.fury.api.FuryUtils;
import com.fury.api.exceptions.FuryDecryptException;
import com.fury.api.exceptions.FuryNotFoundAPPException;
import com.fury.api.exceptions.FuryUpdateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = {"com.mercadolibre.frescos_api_grupo_2_w2.repositories"})
public class DataSourceConfig {
    @Bean
    @Primary
    @Profile({"!local & !integration_test & !test"})
    public DataSource getDataSource(
            final @Value("${spring.datasource.host}") String host,
            final @Value("${spring.datasource.db}") String db,
            final @Value("${spring.datasource.username}") String user,
            final @Value("${spring.datasource.password}") String password
    ) throws FuryDecryptException, FuryNotFoundAPPException, FuryUpdateException {
        return DataSourceBuilder.create()
                .url(String.format("jdbc:mysql://%s/%s?serverTimezone=UTC", FuryUtils.getEnv(host), db))
                .username(user)
                .password(FuryUtils.getEnv(password))
                .build();
    }

    @Bean
    @Primary
    @Profile("integration_test || test")
    public DataSource getDataSourceTest(){
        return DataSourceBuilder.create()
                .url("jdbc:h2:mem:test")
                .username("sa")
                .build();
    }

    @Bean
    @Primary
    @Profile("local")
    public DataSource getDataSourceLocal(
            final @Value("${spring.datasource.host}") String host,
            final @Value("${spring.datasource.db}") String db,
            final @Value("${spring.datasource.username}") String user,
            final @Value("${spring.datasource.password}") String password
    ) {
        return DataSourceBuilder.create()
                .url(String.format("jdbc:mysql://%s/%s?serverTimezone=UTC", host, db))
                .username(user)
                .password(password)
                .build();
    }
}
