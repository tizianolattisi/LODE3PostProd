package it.unitn.lode3.postprod.composition;

import it.unitn.lode3.postprod.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tiziano on 21/10/16.
 */
public class Start extends Application {

    private static Properties properties;

    public static void main(String[] args) {
        try {
            properties = readProprties();
        } catch (IOException e) {
            Logger.getLogger(it.unitn.lode3.postprod.conversion.Start.class.getName()).log(Level.SEVERE, "Unable to open config file.", e);
            System.exit(-1);
        }
        launch(args);
    }

    private static Properties readProprties() throws IOException {
        String propertiesFileName = "/composition.properties";
        InputStream propertiesStream = null;

        try {
            propertiesStream = new FileInputStream(propertiesFileName);
        } catch (FileNotFoundException e) {
            propertiesStream = it.unitn.lode3.postprod.composition.Start.class.getResourceAsStream(propertiesFileName);
        }

        Properties properties = new Properties();
        properties.load(propertiesStream);
        return properties;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/composition.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Controller controller = loader.getController();
        controller.setProperties(properties);
        primaryStage.setTitle("LODE3 - Post Produzione");
        Scene scene = new Scene(root, 680, 400);

        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(controller.handlerClose);
        primaryStage.show();

    }
}
