package it.unitn.lode3.postprod.conversion;

import it.unitn.lode3.postprod.AbstractController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by tiziano on 11/10/16.
 */
public class ConversionController extends AbstractController implements Initializable {

    private ControllerStatus status = ControllerStatus.EMPTY;

    private final String LABEL_TEXT = "Scegli un file da accodare";


    private File selectedFile=null;

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
                ConversionNode node = ConversionNode.create()
                        .ffmpeg(getProperty("ffmpeg.path"))
                        .file(selectedFile)
                        .preset(choiceBoxPreset.getValue().toString());
                if( checkBoxNoAudio.isSelected() ){
                    node = node.noAudio();
                }
                selectedFile=null;
                labelFileName.setText(LABEL_TEXT);
                conversionNodeObservableList.add(node);
                status = ControllerStatus.READY;
                selectFileAsset();
            }
        });
        buttonAvviaCoda.setOnAction(event -> {
            convertingAsset();
            executor = Executors.newFixedThreadPool(1);
            for( ConversionNode node: conversionNodeObservableList ){
                Callable<Integer> task = () -> new ProcessBuilder(node.getCommand()).inheritIO().start().waitFor();
                conversionResults.add(executor.submit(task));
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

                List<ConversionNode> tmp = conversionNodeObservableList.stream().collect(Collectors.toList());
                conversionNodeObservableList.removeAll(conversionNodeObservableList);
                for( ConversionNode node: tmp ){
                    conversionNodeObservableList.add(node);
                }

                if( shutdownExecutor ){
                    shutdown();
                    timeline.stop();
                    status = ControllerStatus.DONE;
                }
                System.out.println("\n");
            }));
            status = ControllerStatus.WAITING;
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
    private void convertingAsset(){
        labelFileName.setText("");
        buttonSfoglia.setDisable(true);
        choiceBoxPreset.setDisable(true);
        checkBoxNoAudio.setDisable(true);
        buttonAccoda.setDisable(true);
        buttonAvviaCoda.setDisable(true);
    }


    /*
     *   UTILITIES
     */


    /*
     *   HANDLERS
     */
    public EventHandler<WindowEvent> handlerClose = event -> {
        if( checkClose() ) {
            shutdown();
        } else {
            event.consume();
        }
    };

    protected Boolean checkClose() {
        if( ControllerStatus.WAITING.equals(status) || ControllerStatus.READY.equals(status) ){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma uscita");
            alert.setHeaderText("Vuoi realmente abbandonare il lavoro di conversione?");
            alert.setContentText("La coda in conversione verrà definitivamente persa.\n\n");
            Optional<ButtonType> result = alert.showAndWait();
            return ButtonType.OK.equals(result.get());
        }
        return Boolean.TRUE;
    }

    protected void shutdown() {
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
