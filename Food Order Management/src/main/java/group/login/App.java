package group.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("client.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void startNew() {
        launch();
    }

    public static void main(String[] args) {
        new App().startNew();
    }
}