package no.kantega;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static no.kantega.DB.*;
import static no.kantega.Util.*;

public class App {
    public static final HikariDataSource datasource = new HikariDataSource();
    public static final int MY_LOCK_ID = 42;

    static {
        datasource.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=sa");
        datasource.setAutoCommit(false);
    }

    public static void main(String[] args) throws Exception {
        Flyway.configure().dataSource(datasource).load().migrate();
        startHttpServer();

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        while (true) {
            int queuelength = executor.getQueue().size();
            printQueueLength(queuelength);
            if (queuelength < 10) try (Connection conn = datasource.getConnection()) {
                runWithLock(conn, MY_LOCK_ID, () -> {

                    List<Password> passwords = queryForList(conn, "select * from password where status='todo' limit 30 ", new PasswordRowMapper());
                    System.out.println("fikk " + passwords.size() + " passord");
                    for (Password password : passwords) {
                        CrackPasswordJob crackPasswordJob = new CrackPasswordJob(password);
                        executor.submit(crackPasswordJob);
                        executeUpdate(conn, "update password set status = 'in_progress' where id = ?", password.id);
                        conn.commit();
                    }
                });
            }
            Thread.sleep(1000);
        }
    }

    private static void runWithLock(Connection conn, int myLockId, ThrowingRunnable runnable) throws Exception {
        try {
            boolean gotLock = queryForObject(conn, "select pg_try_advisory_lock(?)", myLockId);
            printBoolean(gotLock);
            if (gotLock) runnable.run();
            sleepRandom(500, 1500);
        } finally {
            queryForObject(conn, "select pg_advisory_unlock(?)", myLockId);
        }
    }
}
