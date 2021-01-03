package src.dao;

import src.models.User;

public class UserDAO extends BaseDAO {

    public UserDAO(String dbHost, String dbUser, String dbPassword, String dbName) {
        super(dbHost, dbUser, dbPassword, dbName);
    }

    public boolean authenticateUser(User user) {
        // TODO: check credentials
        return true;
    }

}
