package src.endpoints;

import com.sun.net.httpserver.HttpExchange;

public class NotFoundEndpoint extends Endpoint {

    private static String message = "Resource not found";
    private static int statusCode = 404;

    @Override
    public void get(HttpExchange t) {
        String response = "{\"error\": \"" + message +"\"}";
        this.finish(t, NotFoundEndpoint.statusCode, response);
    }

    @Override
    public void head(HttpExchange t) {
        this.finish(t, NotFoundEndpoint.statusCode, null);
    }

    @Override
    public void post(HttpExchange t) {
        String response = "{\"error\": \"" + message +"\"}";
        this.finish(t, NotFoundEndpoint.statusCode, response);
    }

    @Override
    public void put(HttpExchange t) {
        String response = "{\"error\": \"" + message +"\"}";
        this.finish(t, NotFoundEndpoint.statusCode, response);
    }

    @Override
    public void delete(HttpExchange t) {
        String response = "{\"error\": \"" + message +"\"}";
        this.finish(t, NotFoundEndpoint.statusCode, response);
    }

    @Override
    public void connect(HttpExchange t) {
        String response = "{\"error\": \"" + message +"\"}";
        this.finish(t, NotFoundEndpoint.statusCode, response);
    }

    @Override
    public void options(HttpExchange t) {
        String response = "{\"error\": \"" + message +"\"}";
        this.finish(t, NotFoundEndpoint.statusCode, response);
    }

    @Override
    public void trace(HttpExchange t) {
        String response = "{\"error\": \"" + message +"\"}";
        this.finish(t, NotFoundEndpoint.statusCode, response);
    }

    @Override
    public void patch(HttpExchange t) {
        String response = "{\"error\": \"" + message +"\"}";
        this.finish(t, NotFoundEndpoint.statusCode, response);
    }

}
