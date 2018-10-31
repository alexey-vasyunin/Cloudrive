package com.cloudrive.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class settingsController implements Initializable {

    @FXML
    Button folderButton;
    @FXML
    TextField folderTextField;
    @FXML
    Button saveSettingsButton;
    @FXML
    CheckBox autorunCheckbox;
    @FXML
    CheckBox autoconnectCheckbox;
    @FXML
    TextField emailTextField;
    @FXML
    TextField passwordTextField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppSettings client = AppSettings.getInstance();
        folderTextField.setText(client.getPath());
        autoconnectCheckbox.setSelected(client.isConnectOnStart());
        autorunCheckbox.setSelected(client.isStartOnSystem());
        emailTextField.setText(client.getEmail());
        passwordTextField.setText(client.getPassword());
    }

    public void chooseFolder(ActionEvent actionEvent) {

        DirectoryChooser chooser = new DirectoryChooser();
        if (!folderTextField.getText().isEmpty()){
            chooser.setInitialDirectory(Paths.get(folderTextField.getText()).toFile());
        }
        chooser.setTitle("Choose directory");
        File dir = chooser.showDialog(folderButton.getScene().getWindow());
        if (dir != null){
            System.out.println(dir.getAbsolutePath());
            folderTextField.setText(dir.getAbsolutePath());
        }
    }

    public void saveSettings(ActionEvent actionEvent) {
        AppSettings cs = AppSettings.getInstance();
        cs.setPath(folderTextField.getText());
        cs.setConnectOnStart(autoconnectCheckbox.isSelected());
        cs.setStartOnSystem(autorunCheckbox.isSelected());
        cs.setEmail(emailTextField.getText());
        cs.setPassword(passwordTextField.getText());
        cs.save();
        ((Stage)folderButton.getScene().getWindow()).close();
    }
}
