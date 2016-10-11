package it.unitn.lode3.postprod;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiziano on 11/10/16.
 */
public class ConversionNode {

    private final String FFMPEG = "/Applications/ffmpeg/ffmpeg";

    private File fileToConvert;

    private String preset;

    private Boolean noAudio;

    private Long size;

    private String status="-";

    public ConversionNode(File fileToConvert, String preset, Boolean noAudio, Long size) {
        this.fileToConvert = fileToConvert;
        this.preset = preset;
        this.noAudio = noAudio;
        this.size = size;
    }

    public File getFileToConvert() {
        return fileToConvert;
    }

    public String getPreset() {
        return preset;
    }

    public Boolean getNoAudio() {
        return noAudio;
    }

    public Long getSize() {
        return size;
    }

    public String getReadableSize() {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public String getParameters() {
        String parameters=getPreset();
        if( getNoAudio() ){
            parameters += ", no audio";
        }
        return parameters;
    }

    public String getFileName() {
        return fileToConvert.getName();
    }

    public List<String> getCommand() {
        List<String> command = new ArrayList<>();
        command.add(FFMPEG);
        command.add("-i");
        command.add(getFileToConvert().getAbsolutePath());
        if( getNoAudio() ) {
            command.add("-an");
        } else {
            command.add("-strict");
            command.add("-2");
        }
        command.add("-y");
        command.add(getFileName()+".mp4");
        return command;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
