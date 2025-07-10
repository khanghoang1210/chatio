package com.khanghoang.client.presentation.login;


import com.khanghoang.client.presentation.chat.ChatScreenController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginScreenController {

    @FXML private TextField usernameInput;
    @FXML private Label statusLabel;

    @FXML
    private void handleLogin() {
        String username = usernameInput.getText().trim();

        if (username.isEmpty()) {
            statusLabel.setText("Username cannot be empty");
            return;
        }

        try {
            URL url = new URL("http://localhost:8080/users/" + username);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200 || responseCode == 201) {
                statusLabel.setText("Login successful!");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/khanghoang/client/ChatScreen.fxml"));
                Parent root = loader.load();

                ChatScreenController controller = loader.getController();
                controller.setUsername(username);

                Stage stage = (Stage) usernameInput.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Chat - " + username);
                stage.show();
            } else {
                statusLabel.setText("Failed to login. Code: " + responseCode);
            }
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
