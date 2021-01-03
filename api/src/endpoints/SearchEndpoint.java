package src.endpoints;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import java.nio.charset.StandardCharsets;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import src.dao.IndexDAO;

import src.models.WebPage;

public class SearchEndpoint extends Endpoint {

    private static String jsonArray(WebPage[] webPages) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < webPages.length; i++) {
            sb.append(webPages[i].json());
            if (i < (webPages.length - 1)) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static Map<String, String> getQueryMap(String queryString) {
        Map<String, String> result = new HashMap<String, String>();
        if (queryString != null) {
            for (String kvString : queryString.split("&")) {
                String[] kvArray = kvString.split("=");
                if (kvArray.length == 2) {
                    result.put(kvArray[0], kvArray[1]);
                }
            }
        }
        return result;
    }

    @Override
    public void get(HttpExchange t) {
        IndexDAO indexDAO = new IndexDAO("db-server", "postgres", "admin", "postgres");
        Map<String, String> queryMap = SearchEndpoint.getQueryMap(t.getRequestURI().getQuery());
        if (queryMap.containsKey("q")) {
            try {
                String searchQuery = URLDecoder.decode(queryMap.get("q"), StandardCharsets.UTF_8.name());
                this.finish(t, 200, SearchEndpoint.jsonArray(indexDAO.getPages(searchQuery)));
            } catch (SQLException sqle) {
                this.finish(t, 500, "{\"error\": \"Unable to fetch pages\"}");
            } catch (UnsupportedEncodingException uee) {
                this.finish(t, 500, "{\"error\": \"Unable to decode search query\"}");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.finish(t, 400, "{\"error\": \"Search query missing\"}");
        }
    }

}
