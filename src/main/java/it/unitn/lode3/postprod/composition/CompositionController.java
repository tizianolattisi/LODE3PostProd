package it.unitn.lode3.postprod.composition;

import it.unitn.lode3.postprod.AbstractController;
import it.unitn.lode3.postprod.composition.xml.XMLData;
import it.unitn.lode3.postprod.composition.xml.XMLDataInfo;
import it.unitn.lode3.postprod.composition.xml.XMLDataVideo;
import it.unitn.lode3.postprod.composition.xml.XMLHelper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by tiziano on 21/10/16.
 */
public class CompositionController extends AbstractController implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonPcfile;
    @FXML
    private Button buttonCamfile;
    @FXML
    private Button buttonOutputfolder;
    @FXML
    private Label labelPCFileName;
    @FXML
    private Label labelCamFileName;
    @FXML
    private Label labelOutputFolderName;


    private File pcFile=null;
    private File camFile=null;
    private File outputDirectory=null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        buttonStart.setOnAction(event -> {
            compose();
        });
        buttonPcfile.setOnAction(event -> {
            pcFile = selectMovie();
            labelPCFileName.setText(pcFile.getName());
        });
        buttonCamfile.setOnAction(event -> {
            camFile = selectMovie();
            labelCamFileName.setText(camFile.getName());
        });
        buttonOutputfolder.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            outputDirectory = directoryChooser.showDialog(root.getScene().getWindow());
            labelOutputFolderName.setText(outputDirectory.getAbsolutePath());
        });

    }

    private File selectMovie() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("File video (*.mp4)", "*.mp4");
        fileChooser.getExtensionFilters().add(extension);
        return fileChooser.showOpenDialog(root.getScene().getWindow());
    }

    private void compose() {

        // lecture directory
        File lectureDirectory = new File(outputDirectory.getAbsolutePath() + "/lecture");
        lectureDirectory.mkdir();

        // root
        XMLData data = new XMLData();

        // CAM
        XMLDataVideo camVideo = new XMLDataVideo();
        camVideo.setUrl("movie.mp4");
        camVideo.setStartime(0L);
        camVideo.setTotaltime(100L);
        data.setCamvideo(camVideo);

        // PVR
        XMLDataVideo pcVideo = new XMLDataVideo();
        pcVideo.setUrl("movie2.mp4");
        pcVideo.setStartime(0L);
        pcVideo.setTotaltime(100L);
        data.setPcvideo(pcVideo);

        // info
        XMLDataInfo info = new XMLDataInfo();
        info.setCourse("Corso demo");
        info.setLecturer("Docente demo");
        info.setTitle("Titolo demo");
        info.setDynamic_url("http://latemar.science.unitn.it/LODE");
        data.setInfo(info);

        // build
        XMLHelper.build(XMLData.class).marshall(data, new File(lectureDirectory.getAbsolutePath() + "/data.xml"));

        // movie
        try {
            FileUtils.copyFileToDirectory(pcFile, lectureDirectory);
            FileUtils.copyFileToDirectory(camFile, lectureDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
