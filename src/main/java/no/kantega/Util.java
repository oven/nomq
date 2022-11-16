package no.kantega;

import com.sun.net.httpserver.HttpServer;
import org.actioncontroller.httpserver.ApiHandler;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.Random;

class Util {
    public static final Random random = new Random();

    public static void sleepRandom(int minMs, int maxMs) {
        try {
            int sleep = random.nextInt(maxMs - minMs);
            Thread.sleep(minMs + sleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static void startHttpServer() throws IOException {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
            httpServer.createContext("/", new ApiHandler(new MyApiController()));
            httpServer.start();
            System.out.println("Listening for http connections on http://localhost:8080");
        } catch (BindException e) {
            System.out.println("Address already in use, not starting http listener");
        }
    }

    public static void printQueueLength(int size) {
        if (size == 0) return;
        System.out.println(String.format("%1$" + size + "s", 'x').replace(' ', 'x'));
    }

    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String RESET = "\033[0m";  // Text Reset

    public static void printBoolean(boolean b) {
        System.out.println((b ? GREEN_BOLD + "✔︎" : RED_BOLD +  "✘") + RESET);
    }
}
