package src.endpoints;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

public class Endpoint {

    private static String message = "Method not allowed";
    private static int statusCode = 405;

    protected void finish(HttpExchange t, int statusCode, String response) {
        try {
            if (response != null) {
                t.getResponseHeaders().set("Content-Type", "application/json");
                t.sendResponseHeaders(statusCode, response.length());
            }
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void get(HttpExchange t) {
        String response = "{\"error\": \"" + Endpoint.message + "\"}";
        this.finish(t, Endpoint.statusCode, response);
    }

    public void head(HttpExchange t) {
        this.finish(t, Endpoint.statusCode, null);
    }

    public void post(HttpExchange t) {
        String response = "{\"error\": \"" + Endpoint.message + "\"}";
        this.finish(t, Endpoint.statusCode, response);
    }

    public void put(HttpExchange t) {
        String response = "{\"error\": \"" + Endpoint.message + "\"}";
        this.finish(t, Endpoint.statusCode, response);
    }

    public void delete(HttpExchange t) {
        String response = "{\"error\": \"" + Endpoint.message + "\"}";
        this.finish(t, Endpoint.statusCode, response);
    }

    public void connect(HttpExchange t) {
        String response = "{\"error\": \"" + Endpoint.message + "\"}";
        this.finish(t, Endpoint.statusCode, response);
    }

    public void options(HttpExchange t) {
        String response = "{\"error\": \"" + Endpoint.message + "\"}";
        this.finish(t, Endpoint.statusCode, response);
    }

    public void trace(HttpExchange t) {
        String response = "{\"error\": \"" + Endpoint.message + "\"}";
        this.finish(t, Endpoint.statusCode, response);
    }

    public void patch(HttpExchange t) {
        String response = "{\"error\": \"" + Endpoint.message + "\"}";
        this.finish(t, Endpoint.statusCode, response);
    }

    public void handle(HttpExchange t) {
        switch (t.getRequestMethod()) {
            case "GET":
                this.get(t);
                break;
            case "HEAD":
                this.head(t);
                break;
            case "POST":
                this.post(t);
                break;
            case "PUT":
                this.put(t);
                break;
            case "DELETE":
                this.delete(t);
                break;
            case "CONNECT":
                this.connect(t);
                break;
            case "OPTIONS":
                this.options(t);
                break;
            case "TRACE":
                this.trace(t);
                break;
            case "PATCH":
                this.patch(t);
                break;
            default:
                break;
        }
    }

}
