package it.unitn.lode3.postprod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiziano on 11/10/16.
 */
public class Converter {

    private final String FFMPEG = "/Applications/ffmpeg/ffmpeg";

    private ConversionNode node;
    private String outputFileName;

    public Converter(ConversionNode node) {
        this.node = node;
        outputFileName = node.getFileName()+".mp4";
    }

    public List<String> getCommand() {
        List<String> command = new ArrayList<>();
        command.add(FFMPEG);
        command.add("-i");
        command.add(node.getFileToConvert().getAbsolutePath());
        if( node.getNoAudio() ) {
            command.add("-an");
        }
        command.add(node.getFileName()+".mp4");
        return command;
    }

    public void convert() {
        try {
            new ProcessBuilder(getCommand()).start().waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
