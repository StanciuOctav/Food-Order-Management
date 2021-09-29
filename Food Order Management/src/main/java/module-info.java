module group.login {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens group.login to javafx.fxml;
    exports group.login;

    opens group.db to javafx.fxml;
    exports group.db;

    opens group.products to javafx.fxml;
    exports group.products;

    exports group.client;
    opens group.client to javafx.fxml;


}