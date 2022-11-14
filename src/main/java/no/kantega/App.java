package no.kantega;

import com.sun.net.httpserver.HttpServer;
import com.zaxxer.hikari.HikariDataSource;
import org.actioncontroller.httpserver.ApiHandler;
import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static no.kantega.DB.*;

public class App {
    public static final HikariDataSource datasource = new HikariDataSource();

    public static final Random random = new Random();
    public static final int PASSWORD_JOB_ID = 42;

    static {
        datasource.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres?user=postgres&password=sa");
        datasource.setAutoCommit(false);
    }

    public static void main(String[] args) throws Exception {
        Flyway.configure().dataSource(datasource).load().migrate();
        startHttpServer();

        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        while (true) {
            printQueueLength(executorService.getQueue().size());

            if (executorService.getQueue().size() < 10) {
                try (Connection connection = datasource.getConnection()) {
                    boolean gotLock = queryForObject(connection, "select pg_try_advisory_xact_lock(?)", PASSWORD_JOB_ID);
                    System.out.println(gotLock ? " 🔓" : " 🔐");

                    if (gotLock) {
                        List<Password> passwords = queryForList(connection, "select * from password where status = 'todo' limit 30", new PasswordRowMapper());

                        for (Password password : passwords) {
                            executorService.submit(new CrackPasswordJob(password));
                            executeUpdate(connection, "update password set status = 'in_progress' where id = ?", password.getId());
                        }
                    }

                    sleepRandom(500, 1500);
                    connection.commit();
                }
            }

            Thread.sleep(1000);
        }
    }

    private static void startHttpServer() throws IOException {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
            httpServer.createContext("/", new ApiHandler(new MyApiController()));
            httpServer.start();
        } catch (BindException e) {
            System.out.println("Address already in use, not starting http listener");
        }
    }

    private static void printQueueLength(int size) {
        if (size == 0) return;
        System.out.println(String.format("%1$" + size + "s", 'x').replace(' ', 'x'));
    }

    public static void sleepRandom(int minMs, int maxMs) {
        try {
            int sleep = random.nextInt(maxMs - minMs);
            Thread.sleep(minMs + sleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
