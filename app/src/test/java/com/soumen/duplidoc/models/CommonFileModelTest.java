package com.soumen.duplidoc.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Soumen on 26-10-2017.
 */
public class CommonFileModelTest {

    private CommonFileModel commonFileModel = null;

    @Before
    public void setUp() throws Exception {
        commonFileModel = new CommonFileModel();
    }

    @After
    public void tearDown() throws Exception {
        commonFileModel = null;
    }

    @Test
    public void getFilePath() throws Exception {
        String filePath = "storage/0/emulated/abcd";
        commonFileModel.setFilePath(filePath);
        assertEquals(commonFileModel.getFilePath(), filePath);
    }

    @Test
    public void getFileAddedDate() throws Exception {
        long fileAddedDate = 12345678L;
        commonFileModel.setFileAddedDate(fileAddedDate);
        assertEquals(commonFileModel.getFileAddedDate(), fileAddedDate);
    }

    @Test
    public void getFileModifiedDate() throws Exception {
        long fileModifiedDate  = 12345678L;
        commonFileModel.setFileModifiedDate(fileModifiedDate);
        assertEquals(commonFileModel.getFileModifiedDate(), fileModifiedDate);
    }

    @Test
    public void getFileTakenDate() throws Exception {
        long fileTakenDate = 12345678L;
        commonFileModel.setFileTakenDate(fileTakenDate);
        assertEquals(commonFileModel.getFileTakenDate(), fileTakenDate);
    }

    @Test
    public void getFileDisplayName() throws Exception {
        String fileName = "abcd@#$efg.jpg";
        commonFileModel.setFileDisplayName(fileName);
        assertEquals(commonFileModel.getFileDisplayName(), fileName);
    }

    @Test
    public void getFileHeight() throws Exception {
        int height = 400;
        commonFileModel.setFileHeight(height);
        assertEquals(commonFileModel.getFileHeight(), height);
    }

    @Test
    public void getFileWidth() throws Exception {
        int width = 800;
        commonFileModel.setFileWidth(width);
        assertEquals(commonFileModel.getFileWidth(), width);
    }

    @Test
    public void getFileSize() throws Exception {
        int fileSize = 10000;
        commonFileModel.setFileSize(fileSize);
        assertEquals(commonFileModel.getFileSize(), fileSize);
    }

    @Test
    public void getAlbum() throws Exception {
        String album = "cassandra\'s dream";
        commonFileModel.setAlbum(album);
        assertEquals(commonFileModel.getAlbum(), album);
    }

    @Test
    public void getArtist() throws Exception {
        String artist = "John Waite";
        commonFileModel.setArtist(artist);
        assertEquals(commonFileModel.getArtist(), artist);
    }

    @Test
    public void getDuration() throws Exception {
        long duration = 1234;
        commonFileModel.setDuration(duration);
        assertEquals(commonFileModel.getDuration(), duration);
    }
}