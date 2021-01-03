package src.dao;

import src.models.User;
import src.models.WebPage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IndexDAO extends BaseDAO {

    public IndexDAO(String dbHost, String dbUser, String dbPassword, String dbName) {
        super(dbHost, dbUser, dbPassword, dbName);
    }

    public WebPage[] getPages(String titleSubstring) {
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
            sqle.printStackTrace();
        }
        WebPage[] pagesArray = new WebPage[pages.size()];
        return (pages.toArray(pagesArray));
    }

    public boolean addPage(WebPage newPage, User user) {
        UserDAO userDAO = new UserDAO(this.dbHost, this.dbHost, this.dbHost, this.dbHost);
        if (userDAO.authenticateUser(user)) {
            // TODO: add a page
            return false;
        } else {
            return false;
        }
    }

}
