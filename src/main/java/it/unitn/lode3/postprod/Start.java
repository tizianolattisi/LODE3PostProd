package it.unitn.lode3.postprod;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by tiziano on 11/10/16.
 */
public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Controller controller = loader.getController();
        primaryStage.setTitle("LODE3 Post Produzione");
        Scene scene = new Scene(root, 680, 400);

        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(controller.handlerClose);
        primaryStage.show();

    }


}
