package src;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

    private static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response;
            int statusCode;
            switch (t.getRequestURI().getPath()) {
                case "/":
                    t.getResponseHeaders().add("Content-Type", "application/json");
                    response = "{\"result\": \"This is the root URL\"}";
                    statusCode = 200;
                    break;
                case "/search":
                    t.getResponseHeaders().add("Content-Type", "application/json");
                    String q = t.getRequestURI().getQuery();
                    response = "{\"result\": \"Congratulations, you have hit the search API\", \"query\": \"" + q + "\"}";
                    statusCode = 200;
                    break;
                default:
                    t.getResponseHeaders().add("Content-Type", "application/json");
                    response = "{\"error\": \"No resource on this path\", \"status\": \"404\"}";
                    statusCode = 404;
                    break;
            }
            try {
                t.sendResponseHeaders(statusCode, response.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static void serve() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 80), 0);
            server.createContext("/", new MyHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
