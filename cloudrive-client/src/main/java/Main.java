import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileReader;
import java.util.Queue;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        System.out.println(model.getVersion());

        Parent root = FXMLLoader.load(getClass().getResource("/clientMain.fxml"));
        primaryStage.setTitle("Cloudrive v" + model.getVersion());
        Scene scene = new Scene(root, 650, 450);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
