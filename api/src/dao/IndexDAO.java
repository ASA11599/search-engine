package src.dao;

import src.models.User;
import src.models.WebPage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import javax.naming.AuthenticationException;

public class IndexDAO extends BaseDAO {

    public IndexDAO(String dbHost, String dbUser, String dbPassword, String dbName) {
        super(dbHost, dbUser, dbPassword, dbName);
    }

    public WebPage[] getPages(String titleSubstring) throws SQLException {
        List<WebPage> pages = new ArrayList<WebPage>();
        try {
            Statement s = this.dbConnection.createStatement();
            ResultSet rs = s.executeQuery("SELECT Title, Link FROM Index WHERE Title LIKE '%" + titleSubstring + "%'");
            while (rs.next()) {
                String title = rs.getString("Title");
                String link = rs.getString("Link");
                pages.add(new WebPage(title, link));
            }
            rs.close();
            s.close();
        } catch (SQLException sqle) {
            throw sqle;
        }
        WebPage[] pagesArray = new WebPage[pages.size()];
        return (pages.toArray(pagesArray));
    }

    public boolean addPage(WebPage newPage, User user) throws SQLException, AuthenticationException {
        UserDAO userDAO = new UserDAO(this.dbHost, this.dbUser, this.dbPassword, this.dbName);
        try {
            if (userDAO.authenticateUser(user)) {
                try {
                    Statement s = this.dbConnection.createStatement();
                    s.executeUpdate("INSERT INTO Index (Title, Link) VALUES ('" + newPage.getTitle() + "', " + "'" + newPage.getLink() + "')");
                    s.close();
                    return true;
                } catch (SQLException sqle) {
                    throw sqle;
                }
            } else {
                throw new AuthenticationException();
            }
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            userDAO.close();
        }
    }

}
