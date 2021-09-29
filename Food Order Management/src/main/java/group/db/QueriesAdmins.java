package group.db;

import java.sql.*;

public class QueriesAdmins {

    public static final String TABLE_NAME = "admins";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String CHECK_ADMIN = "SELECT _id FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ? AND " + COLUMN_PASSWORD + " =?;";

    public static boolean adminCheck(String name, String password) {

        try (PreparedStatement s = DBConnection.getInstance().getConn().prepareStatement(CHECK_ADMIN)) {
            s.setString(1, name);
            s.setString(2, password);
            ResultSet r = s.executeQuery();
            r.next();
            return r.getInt(1) != 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.getInstance().close();
        }
    }
}
