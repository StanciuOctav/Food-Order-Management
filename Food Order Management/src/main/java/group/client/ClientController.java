package group.client;

import group.db.QueriesClients;
import group.db.QueriesOrders;
import group.db.QueriesProducts;
import group.products.CartProduct;
import group.products.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private TableView<Product> tableView;
    @FXML
    private TableColumn<Product, Integer> _id;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, Integer> price;
    @FXML
    private TableColumn<Product, Integer> inStock;

    @FXML
    private Spinner spinner;

    @FXML
    private TableView<CartProduct> cartTable;
    @FXML
    private TableColumn<CartProduct, String> cartName;
    @FXML
    private TableColumn<CartProduct, Integer> cartQuant;
    @FXML
    private TableColumn<CartProduct, Double> cartPrice;
    @FXML
    private Label totalLabel;
    @FXML
    private Label nrAllOrder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ASTEA SUNT CELE MAI IMPORTANTE PENTRU A PUTEA INITIALIZA TABELA
        _id.setCellValueFactory(new PropertyValueFactory<Product, Integer>("_id"));
        productName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        price.setCellValueFactory(new PropertyValueFactory<Product, Integer>("price"));
        inStock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("inStock"));

        cartName.setCellValueFactory(new PropertyValueFactory<CartProduct, String>("name"));
        cartQuant.setCellValueFactory(new PropertyValueFactory<CartProduct, Integer>("quantity"));
        cartPrice.setCellValueFactory(new PropertyValueFactory<CartProduct, Double>("price"));

        tableView.setItems(QueriesProducts.getAllProducts());
    }

    @FXML
    public void updateSpinnerMaxValue() {
        String productName = tableView.getSelectionModel().getSelectedItem().getName();
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, QueriesProducts.getNrStock(productName), 0));
    }

    @FXML
    public void addProductToCart() {
        // checking if a product was selected
        if (tableView.getSelectionModel().getSelectedIndices().size() > 0) {
            // if yes, the name, quantity and price of the product are extracted
            String pName = tableView.getSelectionModel().getSelectedItem().getName();
            int pQuan = Integer.parseInt(spinner.getEditor().getText());
            float pPrice = Float.parseFloat(String.valueOf(Integer.parseInt(spinner.getEditor().getText()) * tableView.getSelectionModel().getSelectedItem().getPrice()));

            // checking if the quantity is > 0
            if (pQuan > 0) {
                // checking if the selected product was already selected at least once
                for (CartProduct cp : cartTable.getItems()) {
                    if (cp.getName().equals(pName)) {
                        // it the current name matches a name in the cart it means the product was selected before => we need to update the quantity and price
                        float oldPrice = cp.getPrice();
                        int oldQuant = cp.getQuantity();
                        // we remove the old product and we add a new one with the updated fields
                        cartTable.getItems().remove(cp);
                        cartTable.getItems().add(new CartProduct(pName, oldQuant + pQuan, oldPrice + pPrice));
                        tableView.getSelectionModel().clearSelection();
                        updateTotalLabel();
                        tableView.getSelectionModel().clearSelection();
                        return;
                    }
                }
                // if it's not a match then we simply add it in the cart
                cartTable.getItems().add(new CartProduct(pName, pQuan, pPrice));
                updateTotalLabel();
                tableView.getSelectionModel().clearSelection();
                spinner.getValueFactory().setValue(0);
            }
        } else {
            // TODO: de tratat cazul ca nu este nimic selectat
        }
    }

    @FXML
    public void removeProductFromCart() {
        if (cartTable.getSelectionModel().getSelectedIndices().size() > 0) {
            cartTable.getItems().remove(cartTable.getSelectionModel().getSelectedItem());
            cartTable.getSelectionModel().clearSelection();
            updateTotalLabel();
        } else {
            // TODO: de tratat cazul ca nu este nimic selectat
        }
    }

    @FXML
    public void finishOrder() {
        String clientName = ((Stage) (tableView.getScene().getWindow())).getTitle();// asta se foloseste cand incep aplicatia din login.fxml
        for (CartProduct cp : cartTable.getItems()) {
            QueriesOrders.insertOrder(QueriesOrders.countOrders() + 1, QueriesClients.getIdClient(clientName), QueriesProducts.getIdProduct(cp.getName()), cp.getQuantity());
            //QueriesOrders.insertOrder(QueriesOrders.countOrders()+1, QueriesClients.getIdClient("Octav Stanciu"), QueriesProducts.getIdProduct(cp.getName()), cp.getQuantity());
            QueriesProducts.updateProductStock(cp.getQuantity(), cp.getName());
        }
        QueriesClients.updateClientBill(Float.parseFloat(totalLabel.getText().split(":")[1]), clientName);
        totalLabel.setText("Total: ");
        cartTable.getItems().clear();
        nrAllOrder.setText("Nr. of orders: " + QueriesClients.getNrOfOrders(((Stage) (tableView.getScene().getWindow())).getTitle()));
        tableView.setItems(QueriesProducts.getAllProducts());
    }

    public void updateTotalLabel() {
        float total = 0;
        for (CartProduct cp : cartTable.getItems()) {
            total = total + cp.getPrice();
        }
        totalLabel.setText("Total: " + total);
    }

    @FXML
    public void filterByName() {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Search the name");
        td.setHeaderText("Please enter the name:");
        td.setContentText("Name:");
        Optional<String> result = td.showAndWait();
        result.ifPresent(name -> tableView.getItems().setAll(QueriesProducts.getSearchedProductsName("%" + name + "%")));
    }

    @FXML
    public void filterByPrice() {

        TextInputDialog td = new TextInputDialog();
        td.setTitle("Enter the price");
        td.setContentText("Write the price:");

        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Lower", "Equal", "Higher");
        choiceDialog.setTitle("Choice");
        choiceDialog.setHeaderText("Choose the bound");
        choiceDialog.setContentText("Your choice:");

        Optional<String> resultInput = td.showAndWait();
        Optional<String> resultChoice = choiceDialog.showAndWait();

        int choice = 2;
        if (resultInput.isPresent() && Double.parseDouble(resultInput.get()) >= 0) {
            if (resultChoice.isPresent())
                switch (resultChoice.get()) {
                    case "Lower" -> choice = -1;
                    case "Equal" -> choice = 0;
                    case "Higher" -> choice = 1;
                }
            System.out.println(Double.parseDouble(resultInput.get()));
            tableView.setItems(QueriesProducts.getProductsLowerThan(Double.parseDouble(resultInput.get()), choice));
        }
    }

    @FXML
    public void resetProducts() {
        tableView.setItems(QueriesProducts.getAllProducts());
    }

}
