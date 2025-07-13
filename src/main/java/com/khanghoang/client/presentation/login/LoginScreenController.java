package com.khanghoang.client.presentation.login;


import com.khanghoang.client.model.User;
import com.khanghoang.client.presentation.chat.ChatScreenController;
import com.khanghoang.client.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginScreenController {
    private final AuthService authService = new AuthService();
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
            User user = authService.login(username);
            statusLabel.setText("Login successful!");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/khanghoang/client/ChatScreen.fxml"));
            Parent root = loader.load();

            ChatScreenController controller = loader.getController();
            controller.setUser(user);

            Stage stage = (Stage) usernameInput.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Chat - " + username);
            stage.show();

        } catch (Exception e) {
            statusLabel.setText("Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
