package com.khanghoang.client;


import com.khanghoang.client.presentation.chat.ChatScreenController;
import com.khanghoang.client.presentation.login.LoginScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
        Scene scene = new Scene(loader.load());
        LoginScreenController controller = loader.getController();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
