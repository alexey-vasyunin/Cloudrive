package com.cloudrive.client;

import com.cloudrive.client.Client;
import com.cloudrive.client.filelist.FileListItem;
import com.cloudrive.client.filelist.ItemType;
import com.cloudrive.common.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.ResourceBundle;

public class mainController implements Initializable {
    @FXML
    TableView<FileListItem> fileList;

    @FXML
    Button sendButton;
    @FXML
    Button downloadButton;

    private ObservableList<FileListItem> files;

    public PriorityQueue<File> queueFilesSend; // очередь файлов на отправку


    public void btnExit(ActionEvent actionEvent) {
        System.exit(0);
    }


    public void initialize(URL location, ResourceBundle resources) {

        queueFilesSend = new PriorityQueue<>();

        files = FXCollections.observableArrayList();


        TableColumn<FileListItem, String> tcName = new TableColumn<>("Name");
        tcName.setPrefWidth(500);
        tcName.setCellValueFactory(new PropertyValueFactory<FileListItem, String>("name"));

        TableColumn<FileListItem, Integer> tcSize = new TableColumn<>("Size");
        tcSize.setCellValueFactory(new PropertyValueFactory<FileListItem, Integer>("size"));

        fileList.getColumns().addAll(tcName, tcSize);
        fileList.setItems(files);

        /*
            Обрабатываем событие Drag'n'Drop (протаскивание над списком файлов)
         */
        fileList.setOnDragOver(event -> {
            System.out.println(event);
            if (event.getGestureSource() != fileList && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        /*
            Обрабатываем событие, когда файлы скинули в список через драг'n'дроп
         */
        fileList.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean result = false;
            for (int i = 0; i < dragboard.getFiles().size(); i++) {
                Client.getInstance().sendFileToStorage(dragboard.getFiles().get(i));
                System.out.println(dragboard.getFiles().get(i).isDirectory());
                result = true;
            }
            event.setDropCompleted(result);
            event.consume();
        });

        fileList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }


    public void buttonSendClicked(ActionEvent actionEvent) {
        System.out.println("Send (controller)");
    }

    public void downloadButtonAction(ActionEvent actionEvent){
        fileList.getSelectionModel().getSelectedItems().forEach(fileListItem -> {
            System.out.println("Requering " + fileListItem.getName());
            Thread thread = new Thread(() -> {
                for (int i = 0; i < fileListItem.getSize() / Settings.PART_FILE_SIZE; i++) {
                    Client.getInstance().getChannel().writeAndFlush(
                            new PartOfFileRequest(
                                    fileListItem.getName(),
                                    i*Settings.PART_FILE_SIZE,
                                    Settings.PART_FILE_SIZE)
                    );
                }

            });
            thread.setDaemon(true);
            thread.start();
        });
    }

    public void refreshFileList(ArrayList<DirMessage.FileItem> fli) {
        fli.forEach(fileItem -> {
            files.add(new FileListItem(ItemType.FILE, fileItem.getFilename(), (int) fileItem.getSize(), null));
        });
    }


    public void settingsApplication(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoaderConnect = new FXMLLoader(getClass().getResource("/settingsForm.fxml"));
            Parent root = fxmlLoaderConnect.load();
            Scene scene = new Scene(root, 500, 380);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
