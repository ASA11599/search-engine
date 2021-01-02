package src.endpoints;

import com.sun.net.httpserver.HttpExchange;

public class IndexEndpoint extends Endpoint {

    @Override
    public void get(HttpExchange t) {
        this.finish(t, 200, "{\"result\": \"You have reached the index endpoint\"}");
    }

}
