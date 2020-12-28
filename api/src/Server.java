package src;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

    private static byte[] handleRequest(HttpExchange t) {
        String response;
        int statusCode;
        switch (t.getRequestURI().toString()) {
            case "/":
                t.getResponseHeaders().add("Content-Type", "application/json");
                statusCode = 200;
                response = "{\"result\": \"This is the root URL\"}";
                break;
            default:
                t.getResponseHeaders().add("Content-Type", "application/json");
                response = "{\"error\": \"No resource on this path\"}";
                statusCode = 404;
                break;
        }
        try {
            t.sendResponseHeaders(statusCode, response.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.getBytes();
    }

    private static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            byte[] response = Server.handleRequest(t);
            OutputStream os = t.getResponseBody();
            os.write(response);
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
            System.out.println("IOException");
        }
    }

}
