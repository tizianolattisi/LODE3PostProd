package it.unitn.lode3.postprod.composition.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: tiziano
 * Date: 26/01/15
 * Time: 16:41
 */
@XmlRootElement(name = "slide")
public class XMLDataSlide {

    private Long time;
    private String title;
    private String url;

    public XMLDataSlide() {
    }

    public XMLDataSlide(Long time, String title, String url) {
        this.time = time;
        this.title = title;
        this.url = url;
    }

    @XmlElement(name = "title")
    public String getTitle(){
        return title;
    }

    @XmlElement(name = "url")
    public String getUrl(){
        return url;
    }

    @XmlElement(name = "time")
    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
