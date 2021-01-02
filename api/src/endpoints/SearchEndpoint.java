package src.endpoints;

import com.sun.net.httpserver.HttpExchange;

public class SearchEndpoint extends Endpoint {

    @Override
    public void get(HttpExchange t) {
        String q = t.getRequestURI().getQuery();
        this.finish(t, 200, "{\"result\": \"You have reached the search endpoint\", \"query\": \"" + q + "\"}");
    }

}
