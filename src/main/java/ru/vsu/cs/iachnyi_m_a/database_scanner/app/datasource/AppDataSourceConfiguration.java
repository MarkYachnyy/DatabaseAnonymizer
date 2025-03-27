package ru.vsu.cs.iachnyi_m_a.database_scanner.app.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppDataSourceConfiguration {

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/database_scanner");
        config.setUsername("postgres");
        config.setPassword("1234");
        config.setIdleTimeout(20000);
        config.setConnectionTestQuery("SELECT 1");
        config.setMaximumPoolSize(10);
        return config;
    }

    @Bean
    public HikariDataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }
}