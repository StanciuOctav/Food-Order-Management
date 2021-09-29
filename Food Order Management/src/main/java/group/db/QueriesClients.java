package group.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueriesClients {
    public static final String TABLE_NAME = "clients";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_BILL = "total_bill";

    public static final String CHECK_CLIENT = "SELECT "+ COLUMN_ID +" FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ? AND " + COLUMN_PASSWORD + " =?;";
    public static final String QUERY_GETIDCLIENT = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ?;";
    public static final String QUERY_GETBILL = "SELECT " + COLUMN_BILL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ?;";
    public static final String QUERY_UPDATEBILL = "UPDATE " + TABLE_NAME + " SET " + COLUMN_BILL + " = ? WHERE " + COLUMN_NAME + " = ?";
    public static final String QUERY_GETNRORDERS = "SELECT COUNT(*) FROM " + QueriesOrders.TABLE_NAME + " WHERE " + QueriesOrders.COLUMN_CLIENT + " = " +
            "(SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ? )";

    public static boolean clientCheck(String name, String password) {

        try (PreparedStatement s = DBConnection.getInstance().getConn().prepareStatement(CHECK_CLIENT)) {
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

    public static int getIdClient(String name) {
        try {
            PreparedStatement s = DBConnection.getInstance().getConn().prepareStatement(QUERY_GETIDCLIENT);
            s.setString(1, name);
            ResultSet rs = s.executeQuery();

            rs.next();
            int rez = rs.getInt(1);
            rs.close();
            s.close();
            return rez;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public static float getClientBill(String name) {
        try {
            PreparedStatement s = DBConnection.getInstance().getConn().prepareStatement(QUERY_GETBILL);
            s.setString(1, name);
            ResultSet rs = s.executeQuery();

            rs.next();

            float bill = rs.getFloat(1);

            rs.close();
            s.close();
            return bill;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return -1f;
    }

    public static void updateClientBill(float newPrice, String name) {
        try {
            PreparedStatement s = DBConnection.getInstance().getConn().prepareStatement(QUERY_UPDATEBILL);
            s.setFloat(1, getClientBill(name) + newPrice);
            s.setString(2, name);

            s.executeUpdate();

            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static int getNrOfOrders(String name) {
        try{
            PreparedStatement ps = DBConnection.getInstance().getConn().prepareStatement(QUERY_GETNRORDERS);
            ps.setString(1, name);

            int rez = ps.executeQuery().getInt(1);

            ps.close();
            return  rez;

        } catch (SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(getNrOfOrders("Octav Stanciu"));
    }

}
