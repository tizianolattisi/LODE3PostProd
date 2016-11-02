package it.unitn.lode3.postprod.composition.xml;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by tiziano on 26/05/15.
 */
public class XMLDataVideo {

    private String name;
    private Long totaltime;
    private Long startime;

    public XMLDataVideo() {
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    @XmlElement(name = "totaltime")
    public Long getTotaltime() {
        return totaltime;
    }

    @XmlElement(name = "startime")
    public Long getStartime() {
        return startime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotaltime(Long totaltime) {
        this.totaltime = totaltime;
    }

    public void setStartime(Long startime) {
        this.startime = startime;
    }
}
