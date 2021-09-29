package group.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueriesOrders {
    public static final String TABLE_NAME = "orders";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CLIENT = "idClient";
    public static final String COLUMN_PRODUCT = "idProduct";
    public static final String COLUMN_NRORDERED = "nr_ordered";

    public static final String QUERY_COUNTALLORDERS = "SELECT COUNT(*) FROM " + TABLE_NAME;
    public static final String QUERY_INSERTORDER = "INSERT INTO " + TABLE_NAME + " VALUES(?, ?, ?, ?)";

    public static int countOrders() {
        try (Statement s = DBConnection.getInstance().getConn().createStatement();
             ResultSet rs = s.executeQuery(QUERY_COUNTALLORDERS)) {

            if (rs.next())
                return rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public static void insertOrder(int idOrder, int idClient, int idProduct, int nrOrdered) {
        try {
            PreparedStatement s = DBConnection.getInstance().getConn().prepareStatement(QUERY_INSERTORDER);
            s.setInt(1, idOrder);
            s.setInt(2, idClient);
            s.setInt(3, idProduct);
            s.setInt(4, nrOrdered);

            s.executeUpdate();

            s.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(new QueriesOrders().countOrders());
    }
}
