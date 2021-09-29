package group.products;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {
    private SimpleIntegerProperty _id;
    private SimpleStringProperty name;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty inStock;

    public Product(int _id, String name, double price, int inStock) {
        this._id = new SimpleIntegerProperty(_id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.inStock = new SimpleIntegerProperty(inStock);
    }

    public int get_id() {
        return _id.get();
    }

    public void set_id(int _id) {
        this._id.set(_id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public int getInStock() {
        return inStock.get();
    }

    public void setInStock(int inStock) {
        this.inStock.set(inStock);
    }
}
