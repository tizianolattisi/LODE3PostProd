package it.unitn.lode3.postprod.composition;

import it.unitn.lode3.postprod.AbstractController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by tiziano on 21/10/16.
 */
public class CompositionController extends AbstractController implements Initializable {

    @FXML
    private Button buttonStart;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        buttonStart.setOnAction(event -> {

        });
    }

    /*
 *   HANDLERS
 */
    public EventHandler<WindowEvent> handlerClose = event -> {
        if( true ) {
            //
        } else {
            event.consume();
        }
    };


}
