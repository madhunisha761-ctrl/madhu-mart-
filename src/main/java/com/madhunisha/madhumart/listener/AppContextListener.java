package com.madhunisha.madhumart.listener;

import com.madhunisha.madhumart.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        log.info("Starting MadhuMart, creating connection pool");
        DBUtil.init();
        runScript("/db/schema.sql");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        log.info("Stopping MadhuMart, closing connection pool");
        DBUtil.shutdown();
    }

    private void runScript(String path) {
        try (InputStream in = getClass().getResourceAsStream(path)) {
            if (in == null) {
                log.warn("Script not found: {}", path);
                return;
            }
            String sql;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8))) {
                sql = reader.lines().collect(Collectors.joining("\n"));
            }
            try (Connection con = DBUtil.getConnection();
                 Statement st = con.createStatement()) {
                for (String part : sql.split(";")) {
                    String stmt = part.trim();
                    if (!stmt.isEmpty()) {
                        st.execute(stmt);
                    }
                }
            }
            log.info("Script executed: {}", path);
        } catch (Exception e) {
            log.error("Could not run script " + path, e);
            throw new RuntimeException(e);
        }
    }
}
