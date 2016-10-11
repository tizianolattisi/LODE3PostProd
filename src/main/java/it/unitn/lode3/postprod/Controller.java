package it.unitn.lode3.postprod;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.*;

/**
 * Created by tiziano on 11/10/16.
 */
public class Controller implements Initializable{

    private final String LABEL_TEXT = "Scegli un file da accodare";


    private File selectedFile=null;

    //private List<ConversionNode> conversionNodes = new ArrayList<>();

    private List<Future<Integer>> conversionResults = new ArrayList<>();

    /*
     *   WIDGETS
     */
    @FXML
    private VBox root;
    @FXML
    private Button buttonSfoglia;
    @FXML
    private Button buttonAccoda;
    @FXML
    private Button buttonAvviaCoda;
    @FXML
    private Button buttonAnnulla;
    @FXML
    private Label labelFileName;
    @FXML
    private TableView tableViewCoda;
    @FXML
    private TableColumn<ConversionNode, String> tableColumnFileName;
    @FXML
    private TableColumn<ConversionNode, String> tableColumnParametri;
    @FXML
    private TableColumn<ConversionNode, String> tableColumnDimensione;
    @FXML
    private TableColumn<ConversionNode, String> tableColumnStato;
    @FXML
    private CheckBox checkBoxNoAudio;
    @FXML
    private ChoiceBox choiceBoxPreset;
    private ExecutorService executor;
    private ObservableList<ConversionNode> conversionNodeObservableList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tableColumnFileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        tableColumnParametri.setCellValueFactory(new PropertyValueFactory<>("parameters"));
        tableColumnDimensione.setCellValueFactory(new PropertyValueFactory<>("readableSize"));
        tableColumnStato.setCellValueFactory(new PropertyValueFactory<>("status"));

        labelFileName.setText(LABEL_TEXT);

        choiceBoxPreset.setItems(FXCollections.observableArrayList("ultrafast", "superfast", "veryfast", "faster",
                "fast", "medium", "slow", "slower", "veryslow", "placebo")
        );
        choiceBoxPreset.setValue("fast");

        //conversionNodeObservableList = FXCollections.observableArrayList(this.conversionNodes);
        conversionNodeObservableList = FXCollections.observableArrayList();
        tableViewCoda.setItems(conversionNodeObservableList);

        buttonSfoglia.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("File video (*.mov, *.m2ts)", "*.mov", "*.m2ts");
            fileChooser.getExtensionFilters().add(extension);
            selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
            labelFileName.setText(selectedFile.getName());
            selectedFileAsset();
        });

        buttonAccoda.setOnAction(event -> {
            if( selectedFile!=null ){
                ConversionNode node = new ConversionNode(selectedFile, choiceBoxPreset.getValue().toString(), checkBoxNoAudio.isSelected(), selectedFile.length());
                //this.conversionNodes.add(node);
                selectedFile=null;
                labelFileName.setText(LABEL_TEXT);
                conversionNodeObservableList.add(node);
                //conversionNodeObservableList = FXCollections.observableArrayList(this.conversionNodes);
                //tableViewCoda.setItems(conversionNodeObservableList);
                selectFileAsset();
            }
        });
        buttonAvviaCoda.setOnAction(event -> {
            executor = Executors.newFixedThreadPool(1);
            for( ConversionNode node: conversionNodeObservableList ){
                Callable<Integer> task = () -> new ProcessBuilder(node.getCommand()).inheritIO().start().waitFor();
                conversionResults.add(executor.submit(task));
                System.out.println("Aggiunto " + node.getFileName());
            }

            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), timelineEvent -> {
                Integer i=0;
                Boolean shutdownExecutor = Boolean.TRUE;
                for( Future<Integer> res: conversionResults ){
                    shutdownExecutor = shutdownExecutor && res.isDone();
                    if( res.isDone() ) {
                        conversionNodeObservableList.get(i).setStatus("Fatto.");
                    } else {
                        conversionNodeObservableList.get(i).setStatus("Attesa...");
                    }
                    i++;
                }

                for( ConversionNode node: conversionNodeObservableList ){
                    System.out.println(node.getFileName() + " -> " + node.getStatus());
                }
                if( shutdownExecutor ){
                    shutdownExecutor();
                    timeline.stop();
                }
                System.out.println("\n");
            }));
            timeline.play();

        });
        selectFileAsset();
    }

    private void selectFileAsset(){
        buttonSfoglia.setDisable(false);
        choiceBoxPreset.setDisable(true);
        checkBoxNoAudio.setDisable(true);
        buttonAccoda.setDisable(true);
        buttonAvviaCoda.setDisable(conversionNodeObservableList.size()==0);
    }

    private void selectedFileAsset(){
        buttonSfoglia.setDisable(false);
        choiceBoxPreset.setDisable(false);
        checkBoxNoAudio.setDisable(false);
        buttonAccoda.setDisable(false);
        buttonAvviaCoda.setDisable(conversionNodeObservableList.size()==0);
    }


    /*
     *   UTILITIES
     */


    /*
     *   HANDLERS
     */
    public EventHandler<WindowEvent> handlerClose = event -> {
        if( conversionNodeObservableList.size()>0 ){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma uscita");
            alert.setHeaderText("Vuoi realmente abbandonare il lavoro di conversione?");
            alert.setContentText("La coda in conversione verrà definitivamente persa.\n\n");
            Optional<ButtonType> result = alert.showAndWait();
            if (ButtonType.OK.equals(result.get())) {
                shutdownExecutor();
                return;
            } else {
                event.consume();
            }
        }
    };

    private void shutdownExecutor() {
        if( executor!=null ) {
            executor.shutdown();
            try {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {

            } finally {
                executor.shutdownNow();
            }
        }
    }

}
