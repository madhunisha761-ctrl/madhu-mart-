package com.madhunisha.madhumart.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class DBUtil {

    private static HikariDataSource dataSource;

    private DBUtil() {
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public static synchronized void init() {
        if (dataSource != null) {
            return;
        }
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl(env("DB_URL", "jdbc:h2:file:./data/madhumart;DB_CLOSE_DELAY=-1"));
        config.setUsername(env("DB_USER", "sa"));
        config.setPassword(env("DB_PASSWORD", ""));
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setPoolName("MadhuMartPool");
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("Connection pool is not started");
        }
        return dataSource.getConnection();
    }

    public static synchronized void shutdown() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }
}
