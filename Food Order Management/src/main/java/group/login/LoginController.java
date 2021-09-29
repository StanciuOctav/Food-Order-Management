package group.login;

import group.db.QueriesAdmins;
import group.db.QueriesClients;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button cancelButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private RadioButton radioButtonAdmin;
    @FXML
    private RadioButton radioButtonClient;
    @FXML
    private BorderPane borderPane;

    @FXML
    public void closeApp() {
        Stage s = (Stage) cancelButton.getScene().getWindow();
        s.close();
    }

    @FXML
    public void loginApp() throws IOException {
        if (radioButtonAdmin.isSelected() && QueriesAdmins.adminCheck(usernameTextField.getText(), passwordTextField.getText())) {
            System.out.println("Logat admin");
            closeApp();
        } else if (radioButtonClient.isSelected() && QueriesClients.clientCheck(usernameTextField.getText(), passwordTextField.getText())) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("client.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Stage stage = (Stage) (borderPane.getScene().getWindow());
            stage.setTitle(usernameTextField.getText());
            stage.setScene(scene);
            stage.show();
        }
    }

}