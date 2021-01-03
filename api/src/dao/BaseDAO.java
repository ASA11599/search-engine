package src.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDAO {

    protected String dbHost;
    protected String dbUser;
    protected String dbPassword;
    protected String dbName;

    protected Connection dbConnection;

    public BaseDAO(String dbHost, String dbUser, String dbPassword, String dbName) {
        this.dbHost = dbHost;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.dbName = dbName;
        this.connect();
    }

    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            this.dbConnection = DriverManager.getConnection("jdbc:postgresql://" + this.dbHost + ":5432/" + this.dbName, this.dbUser, this.dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.dbConnection.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

}
