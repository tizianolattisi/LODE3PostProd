package it.unitn.lode3.postprod.composition.xml;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by tiziano on 26/05/15.
 */
public class XMLDataVideo {

    private String url;
    private Long totaltime;
    private Long startime;

    public XMLDataVideo() {
    }

    @XmlElement(name = "url")
    public String getUrl() {
        return url;
    }

    @XmlElement(name = "totaltime")
    public Long getTotaltime() {
        return totaltime;
    }

    @XmlElement(name = "startime")
    public Long getStartime() {
        return startime;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTotaltime(Long totaltime) {
        this.totaltime = totaltime;
    }

    public void setStartime(Long startime) {
        this.startime = startime;
    }
}
