package com.soumen.duplidoc.models;

import java.io.Serializable;

/**
 * Created by Soumen on 17-10-2017.
 */

public class CommonFileModel implements Serializable {

    private String filePath;
    private String fileDisplayName;
    private long fileAddedDate;
    private long fileModifiedDate;
    private long fileTakenDate;
    private int fileHeight;
    private int fileWidth;
    private long fileSize;
    private String album;
    private String artist;
    private long duration;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileAddedDate() {
        return fileAddedDate;
    }

    public void setFileAddedDate(long fileAddedDate) {
        this.fileAddedDate = fileAddedDate;
    }

    public long getFileModifiedDate() {
        return fileModifiedDate;
    }

    public void setFileModifiedDate(long fileModifiedDate) {
        this.fileModifiedDate = fileModifiedDate;
    }

    public long getFileTakenDate() {
        return fileTakenDate;
    }

    public void setFileTakenDate(long fileTakenDate) {
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

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
