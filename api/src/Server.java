package src;

import src.endpoints.Endpoint;
import src.endpoints.IndexEndpoint;
import src.endpoints.SearchEndpoint;
import src.endpoints.NotFoundEndpoint;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

    private static Map<String, Endpoint> routes = new HashMap<String, Endpoint>();
    static {
        routes.put("/search", new SearchEndpoint());
        routes.put("/index", new IndexEndpoint());
    }

    private static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().getPath().split("\\?")[0];
            Endpoint endpoint;
            if (routes.containsKey(path)) {
                endpoint = routes.get(path);
            } else {
                endpoint = new NotFoundEndpoint();
            }
            endpoint.handle(t);
        }
    }

    public static void serve() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getByName("192.168.64.12"), 8080), 0);
            server.createContext("/", new MyHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
