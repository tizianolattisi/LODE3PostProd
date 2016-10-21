package it.unitn.lode3.postprod.composition.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: tiziano
 * Date: 26/01/15
 * Time: 16:34
 */

@XmlRootElement(name = "data")
public class XMLData {

    private XMLDataVideo camvideo = new XMLDataVideo();

    private XMLDataVideo pcvideo = new XMLDataVideo();

    private XMLDataInfo info = new XMLDataInfo();

    public XMLData() {
    }

    @XmlElement(name = "slide")
    private List<XMLDataSlide> slides = new ArrayList<>();

    public void addSlide(XMLDataSlide s){
        slides.add(s);
    }

    @XmlElement(name = "camvideo")
    public XMLDataVideo getCamvideo() {
        return camvideo;
    }

    @XmlElement(name = "pcvideo")
    public XMLDataVideo getPcvideo() {
        return pcvideo;
    }

    @XmlElement(name = "info")
    public XMLDataInfo getInfo() {
        return info;
    }

    public void setCamvideo(XMLDataVideo camvideo) {
        this.camvideo = camvideo;
    }

    public void setPcvideo(XMLDataVideo pcvideo) {
        this.pcvideo = pcvideo;
    }

    public void setInfo(XMLDataInfo info) {
        this.info = info;
    }
}
