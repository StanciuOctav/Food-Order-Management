package group.db;

import java.sql.*;

public class DBConnection {

    private final String CONNECTION_STRING = "jdbc:sqlite:DB Resources/food.db";
    private Connection con = null;

    private static DBConnection dbcon = null;

    public static DBConnection getInstance() {
        if (dbcon == null)
            dbcon = new DBConnection();
        return dbcon;
    }

    public Connection getConn() {
        try {
            con = DriverManager.getConnection(CONNECTION_STRING);
            return con;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database!");
            return null;
        }
    }

    public void close() {
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
            System.out.println("Couldn't close the databse!");
        }
    }
}
