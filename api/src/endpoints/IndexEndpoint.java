package src.endpoints;

import java.io.IOException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.SQLException;

import java.util.Base64;

import javax.naming.AuthenticationException;

import com.sun.net.httpserver.HttpExchange;

import src.dao.IndexDAO;

import src.models.User;
import src.models.WebPage;

public class IndexEndpoint extends Endpoint {

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] hash = md.digest();
            md.reset();
            StringBuilder sb = new StringBuilder(hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return null;
        }
    }

    private static WebPage parseWebPageJSON(String wpJSON) throws IllegalArgumentException {
        // TODO: parse the given JSON string
        return new WebPage("www.com", "http://www.com");
    }

    @Override
    public void post(HttpExchange t) {
        String authorizationHeader = t.getRequestHeaders().getFirst("Authorization");
        if (authorizationHeader == null) {
            this.finish(t, 401, "{\"error\": \"Unauthorized\"}");
        } else {
            String contentTypeHeader = t.getRequestHeaders().getFirst("Content-Type");
            if ((contentTypeHeader == null) || (!contentTypeHeader.equals("application/json"))) {
                this.finish(t, 400, "{\"error\": \"Invalid request body format, use JSON\"}");
            } else {
                String encodedCredentials = (authorizationHeader.split(" ")[0].equals("Basic")) ? (authorizationHeader.split(" ")[1]) : ("");
                String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials));
                if (decodedCredentials.split(":").length == 2) {
                    String username = decodedCredentials.split(":")[0];
                    String password = decodedCredentials.split(":")[1];
                    String passwordHash = IndexEndpoint.hashPassword(password);
                    try {
                        String body = new String(t.getRequestBody().readAllBytes());
                        WebPage newPage = IndexEndpoint.parseWebPageJSON(body);
                        IndexDAO indexDAO = new IndexDAO("db-server", "postgres", "admin", "postgres");
                        boolean success = indexDAO.addPage(newPage, new User(username, passwordHash));
                        indexDAO.close();
                        if (success) this.finish(t, 200, body);
                        else this.finish(t, 500, "{\"error\": \"Unable to add page\"}");
                    } catch (IOException e) {
                        this.finish(t, 500, "{\"error\": \"Unable to read request body\"}");
                    } catch (IllegalArgumentException iae) {
                        this.finish(t, 400, "{\"error\": \"Invalid request body\"}");
                    } catch (SQLException sqle) {
                        this.finish(t, 500, "{\"error\": \"Unable to add page\"}");
                    } catch (AuthenticationException ae) {
                        this.finish(t, 403, "{\"error\": \"Forbidden\"}");
                    } catch (Exception e) {
                        this.finish(t, 500, "{\"error\": \"Internal server error\"}");
                    }
                } else {
                    this.finish(t, 400, "{\"error\": \"Unable to decode credentials\"}");
                }
            }
        }
    }

}
