package com.soumen.duplidoc.models;

import java.io.Serializable;

/**
 * Created by Soumen on 17-10-2017.
 */

public class CommonFileModel implements Serializable {

    private String filePath;
    private String fileDisplayName;
    private String fileAddedDate;
    private String fileModifiedDate;
    private String fileTakenDate;
    private int fileHeight;
    private int fileWidth;
    private int fileSize;
    private String album;
    private String artist;
    private String duration;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileAddedDate() {
        return fileAddedDate;
    }

    public void setFileAddedDate(String fileAddedDate) {
        this.fileAddedDate = fileAddedDate;
    }

    public String getFileModifiedDate() {
        return fileModifiedDate;
    }

    public void setFileModifiedDate(String fileModifiedDate) {
        this.fileModifiedDate = fileModifiedDate;
    }

    public String getFileTakenDate() {
        return fileTakenDate;
    }

    public void setFileTakenDate(String fileTakenDate) {
        this.fileTakenDate = fileTakenDate;
    }

    public String getFileDisplayName() {
        return fileDisplayName;
    }

    public void setFileDisplayName(String fileDisplayName) {
        this.fileDisplayName = fileDisplayName;
    }

    public int getFileHeight() {
        return fileHeight;
    }

    public void setFileHeight(int fileHeight) {
        this.fileHeight = fileHeight;
    }

    public int getFileWidth() {
        return fileWidth;
    }

    public void setFileWidth(int fileWidth) {
        this.fileWidth = fileWidth;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
