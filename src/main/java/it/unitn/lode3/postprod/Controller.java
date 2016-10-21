package it.unitn.lode3.postprod;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

import java.util.Properties;

/**
 * Created by tiziano on 21/10/16.
 */
public interface Controller {

    EventHandler<WindowEvent> handlerClose = event -> {};

    void setProperties(Properties properties);

    String getProperty(String name);

}
