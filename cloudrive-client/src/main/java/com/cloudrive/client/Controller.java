package com.cloudrive.client;

import com.cloudrive.client.Client;
import com.cloudrive.client.filelist.FileListItem;
import com.cloudrive.client.filelist.ItemType;
import com.cloudrive.common.DirMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    TableView<FileListItem> fileList;

    @FXML
    Button send;

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
    }


    public void buttonSendClicked(ActionEvent actionEvent) {
        System.out.println("Send (controller)");
        //Client.getInstance().send();
    }

    public void refreshFileList(ArrayList<DirMessage.FileItem> fli) {
        fli.forEach(fileItem -> {
            files.add(new FileListItem(ItemType.FILE, fileItem.getFilename(), (int) fileItem.getSize(), null));
        });
    }
}