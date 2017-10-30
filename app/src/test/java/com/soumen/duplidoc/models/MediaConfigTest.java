package com.soumen.duplidoc.models;

import android.provider.MediaStore;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Soumen on 26-10-2017.
 */
public class MediaConfigTest {

    private MediaConfig mediaConfig = null;
    private String[] columns = new String[] {MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Audio.Media.SIZE, MediaStore.Video.Media.DURATION};
    private String orderBy = "someOrder";

    @Before
    public void setUp() throws Exception {
        mediaConfig = new MediaConfig(columns, orderBy);
    }

    @After
    public void tearDown() throws Exception {
        mediaConfig = null;
    }

    @Test
    public void getMediaConfig() throws Exception {
        assertEquals(mediaConfig.getMediaConfig(), mediaConfig);
    }

    @Test
    public void getColumns() throws Exception {
        assertEquals(mediaConfig.getColumns()[0], columns[0]);
        assertEquals(mediaConfig.getColumns()[1], columns[1]);
        assertEquals(mediaConfig.getColumns()[2], columns[2]);
    }

    @Test
    public void getOrderBy() throws Exception {
        assertEquals(mediaConfig.getOrderBy(), orderBy);
    }
}