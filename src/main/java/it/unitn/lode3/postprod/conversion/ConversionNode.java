package it.unitn.lode3.postprod.conversion;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiziano on 11/10/16.
 */
public class ConversionNode {

    private String ffmpegPath = "ffmpeg";

    private File fileToConvert;

    private String preset;

    private Boolean noAudio = Boolean.FALSE;

    private Long size;

    private String status="-";

    public ConversionNode() {
    }

    public static ConversionNode create() {
        return new ConversionNode();
    }

    public ConversionNode file(File fileToConvert) {
        this.fileToConvert = fileToConvert;
        this.size = fileToConvert.length();
        return this;
    }

    public ConversionNode preset(String preset) {
        this.preset = preset;
        return this;
    }

    public ConversionNode noAudio() {
        this.noAudio = Boolean.TRUE;
        return this;
    }

    public ConversionNode ffmpeg(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
        return this;
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
        command.add(ffmpegPath);
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
