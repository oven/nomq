package no.kantega;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import java.sql.Connection;

import static no.kantega.DB.queryForObject;
import static no.kantega.Util.*;

public class App {
    public static final HikariDataSource datasource = new HikariDataSource();

    static {
        datasource.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=sa");
        datasource.setAutoCommit(false);
    }

    public static void main(String[] args) throws Exception {
        Flyway.configure().dataSource(datasource).load().migrate();
        startHttpServer();

        while (true) {
            try (Connection conn = datasource.getConnection()) {
                var result = queryForObject(conn, "select now()");
                System.out.println(result);
            }
            Thread.sleep(1000);
        }
    }
}
