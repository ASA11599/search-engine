package src.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import src.models.User;

public class UserDAO extends BaseDAO {

    public UserDAO(String dbHost, String dbUser, String dbPassword, String dbName) {
        super(dbHost, dbUser, dbPassword, dbName);
    }

    public boolean authenticateUser(User user) throws SQLException {
        try {
            String username = null;
            String passwordHash = null;
            Statement s = this.dbConnection.createStatement();
            ResultSet rs = s.executeQuery("SELECT Username, PasswordHash FROM Administrators WHERE Username='" + user.getUsername() + "'");
            while (rs.next()) {
                username = rs.getString("Username");
                passwordHash = rs.getString("PasswordHash");
            }
            rs.close();
            s.close();
            if ((username == null) || (passwordHash == null)) {
                return false;
            } else {
                return (user.getUsername().equals(username) && (user.getPasswordHash().equals(passwordHash)));
            }
        } catch (SQLException sqle) {
            throw sqle;
        } catch (NullPointerException npe) {
            return false;
        }
    }

}
