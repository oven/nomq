package no.kantega;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static no.kantega.DB.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DbTest {
    static PostgreSQLContainer<?> postgres;
    static HikariDataSource datasource;
    Connection connection;

    @BeforeAll
    static void initDatabase() throws Exception {
        postgres = new PostgreSQLContainer<>("postgres:13");
        postgres.start();

        datasource = new HikariDataSource();
        datasource.setUsername(postgres.getUsername());
        datasource.setPassword(postgres.getPassword());
        datasource.setJdbcUrl(postgres.getJdbcUrl());
        datasource.setAutoCommit(false);

        int retries = 10;
        while (retries > 0) {
            try (Connection connection = datasource.getConnection()) {
                var time = queryForObject(connection, "select now()");
                System.out.println(time);
                Thread.sleep(200);
                break;
            } catch (SQLException e) {
                retries--;
            }
        }

        Flyway.configure().dataSource(datasource).load().migrate();

        assertTrue(retries > 0, "Database was not ready");
    }

    @BeforeEach
    void getConnection() throws Exception {
        connection = datasource.getConnection();
    }

    @Test
    void test_something_using_db() throws Exception {
        executeUpdate(connection, "insert into password(hashed_password, status) values (?,?)", "test", "todo");

        List<Password> passwords = queryForList(connection, "select * from password", new PasswordRowMapper());
        assertEquals(1, passwords.size());
        assertEquals("todo", passwords.get(0).status);
    }

    @AfterEach
    void rollback() throws Exception {
        connection.rollback();
    }

    @AfterAll
    static void shutdown() throws Exception {
        postgres.close();
    }
}
