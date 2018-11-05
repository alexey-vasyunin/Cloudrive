import com.cloudrive.client.Client;
import com.cloudrive.client.AppSettings;
import com.cloudrive.client.mainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileReader;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private mainController mc;

    public void start(Stage primaryStage) throws Exception {

        if (AppSettings.getInstance().isLoaded()){
            System.out.println("Конфигурационный файл загружен успешно");
        } else {
            System.out.println("Вознилка проблема с загрузкой конфигурационного файла");
        }

        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/clientMain.fxml"));
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("Cloudrive v" + model.getVersion());
        Scene scene = new Scene(root, 650, 400);
        primaryStage.setScene(scene);



        Client client = Client.getInstance();
        client.setMainController(fxmlLoader.getController());
        Thread thread = new Thread(client);
        thread.setDaemon(true);
        thread.start();

        scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            if (thread.isAlive()) thread.interrupt();
        });

        primaryStage.show();
    }

}
