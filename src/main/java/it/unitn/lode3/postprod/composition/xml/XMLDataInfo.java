package it.unitn.lode3.postprod.composition.xml;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by tiziano on 26/05/15.
 */
public class XMLDataInfo {

    private String course;
    private String title;
    private String lecturer;
    private String dynamic_url;

    public XMLDataInfo() {
    }

    @XmlElement(name = "course")
    public String getCourse() {
        return course;
    }

    @XmlElement(name = "title")
    public String getTitle() {
        return title;
    }

    @XmlElement(name = "lecturer")
    public String getLecturer() {
        return lecturer;
    }

    @XmlElement(name = "dinamic_url") // XXX: correggere?
    public String getDynamic_url() {
        return dynamic_url;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public void setDynamic_url(String dynamic_url) {
        this.dynamic_url = dynamic_url;
    }
}
