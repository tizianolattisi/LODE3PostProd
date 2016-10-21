package it.unitn.lode3.postprod;

import java.util.Properties;

/**
 * Created by tiziano on 21/10/16.
 */
public abstract class AbstractController implements Controller {

    private Properties properties;

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

}