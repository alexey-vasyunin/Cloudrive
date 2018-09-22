import com.cloudrive.client.filelist.FileListItem;
import com.cloudrive.client.filelist.ItemType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    TableView<FileListItem> fileList;

    public void btnExit(ActionEvent actionEvent) {
        System.exit(0);
    }


    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<FileListItem> files = FXCollections.observableArrayList();
        files.addAll(
                new FileListItem(ItemType.DIRECTORY, "rootDir", null, null),
                new FileListItem(ItemType.FILE, "pasport.jpg", 50240, null)
        );

        TableColumn<FileListItem, String> tcName  = new TableColumn<>("Name");
        tcName.setPrefWidth(500);
        tcName.setCellValueFactory(new PropertyValueFactory<FileListItem, String>("name"));

        TableColumn<FileListItem, Integer> tcSize  = new TableColumn<>("Size");
        tcSize.setCellValueFactory(new PropertyValueFactory<FileListItem, Integer>("size"));

        fileList.getColumns().addAll(tcName, tcSize);
        fileList.setItems(files);
    }
}
