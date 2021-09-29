package group.db;

import group.products.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

public class QueriesProducts {
    public static final String TABLE_NAME = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_STOCK = "stock";

    public static final String QUERY_GETPRODUCTS = "SELECT * FROM " + TABLE_NAME;
    public static final String QUERY_GETSTOCK = "SELECT * FROM " + TABLE_NAME + " WHERE name = ?;";
    public static final String QUERY_GETIDPRODUCT = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ?;";
    public static final String QUERY_GETSTOCKPRODUCT = "UPDATE " + TABLE_NAME + " SET " + COLUMN_STOCK + " = ? WHERE " + COLUMN_NAME + " = ?;";
    public static final String QUERY_FILTER_NAME = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " LIKE ?;";
    public static final String QUERY_FILTER_PRICE_LOWER = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PRICE + " < ?";
    public static final String QUERY_FILTER_PRICE_EQUAL = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PRICE + " = ?";
    public static final String QUERY_FILTER_PRICE_GREATER = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PRICE + " > ?";

    // choice = -1 => LOWER       choice = 0 => EQUAL     choice = 1 => GREATER
    public static ObservableList<Product> getProductsLowerThan(double price, int choice) {
        ArrayList<Product> products = new ArrayList<>();
        try {
            PreparedStatement s;
            switch (choice){
                case -1 -> s = DBConnection.getInstance().getConn().prepareStatement(QUERY_FILTER_PRICE_LOWER);
                case 0 -> s = DBConnection.getInstance().getConn().prepareStatement(QUERY_FILTER_PRICE_EQUAL);
                case 1 -> s = DBConnection.getInstance().getConn().prepareStatement(QUERY_FILTER_PRICE_GREATER);
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            }
            s.setDouble(1, price);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                if (rs.getInt(4) > 0)
                    products.add(new Product(rs.getInt(COLUMN_ID), rs.getString(COLUMN_NAME), rs.getDouble(COLUMN_PRICE), rs.getInt(COLUMN_STOCK)));
            }
            return FXCollections.observableArrayList(products);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<Product> getSearchedProductsName(String name) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        try {
            PreparedStatement s = DBConnection.getInstance().getConn().prepareStatement(QUERY_FILTER_NAME);
            s.setString(1, name);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                filteredProducts.add(new Product(rs.getInt(1), rs.getString(2), rs.getFloat(3), rs.getInt(4)));
            }

            if (filteredProducts.size() > 0) {
                rs.close();
                s.close();
                return FXCollections.observableArrayList(filteredProducts);
            } else {
                rs.close();
                s.close();
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();

        try (Statement s = DBConnection.getInstance().getConn().createStatement();
             ResultSet rs = s.executeQuery(QUERY_GETPRODUCTS)) {

            while (rs.next()) {
                if (rs.getInt(COLUMN_STOCK) > 0)
                    products.add(new Product(rs.getInt(COLUMN_ID), rs.getString(COLUMN_NAME), rs.getDouble(COLUMN_PRICE), rs.getInt(COLUMN_STOCK)));
            }

        } catch (SQLException e) {
            System.out.println("Couldn't get all products");
            e.printStackTrace();
        }

        DBConnection.getInstance().close();
        return FXCollections.observableArrayList(products);
    }

    public static int getNrStock(String productName) {

        try {
            PreparedStatement ps = DBConnection.getInstance().getConn().prepareStatement(QUERY_GETSTOCK);
            ps.setString(1, productName);
            ResultSet resultSet = ps.executeQuery();

            int stock = 0;
            resultSet.next();
            stock = resultSet.getInt(COLUMN_STOCK);

            resultSet.close();
            ps.close();
            return stock;
        } catch (SQLException e) {
            System.out.println("Couldn't return the total number in stock for " + productName.toUpperCase(Locale.ROOT));
            e.printStackTrace();
        }
        return -1;
    }

    public static int getIdProduct(String name) {
        try {
            PreparedStatement s = DBConnection.getInstance().getConn().prepareStatement(QUERY_GETIDPRODUCT);
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

    public static void updateProductStock(int nrOrdered, String name) {
        try {
            PreparedStatement s = DBConnection.getInstance().getConn().prepareStatement(QUERY_GETSTOCKPRODUCT);
            s.setInt(1, getNrStock(name) - nrOrdered);
            s.setString(2, name);

            s.executeUpdate();

            s.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            //PreparedStatement s = DBConnection.getInstance().getConn().prepareStatement(QUERY_FILTER_NAME);
            //s.setString(1, "%paine%");
            Statement s = DBConnection.getInstance().getConn().createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM products WHERE price = 9.99");
            while(rs.next()){
                    System.out.println(rs.getFloat(3));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
