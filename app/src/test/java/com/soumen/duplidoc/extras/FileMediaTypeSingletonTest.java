package com.soumen.duplidoc.extras;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Soumen on 26-10-2017.
 */
public class FileMediaTypeSingletonTest {

    private FileMediaTypeSingleton fileMediaTypeSingleton = null;

    @Before
    public void setUp() throws Exception {
        fileMediaTypeSingleton = FileMediaTypeSingleton.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        fileMediaTypeSingleton = null;
    }

    @Test
    public void getInstance() throws Exception {
        assertNotNull(fileMediaTypeSingleton);
        assertEquals(FileMediaTypeSingleton.getInstance(), fileMediaTypeSingleton);
    }

    @Test
    public void createAndReturnImageConfig() throws Exception {
        assertNotNull(fileMediaTypeSingleton.createAndReturnImageConfig());

        assertEquals(fileMediaTypeSingleton.createAndReturnImageConfig().getColumns(),
                FileMediaTypeSingleton.getInstance().createAndReturnImageConfig().getColumns());

        assertEquals(fileMediaTypeSingleton.createAndReturnImageConfig().getOrderBy(),
                FileMediaTypeSingleton.getInstance().createAndReturnImageConfig().getOrderBy());

        assertNotEquals(fileMediaTypeSingleton.createAndReturnImageConfig(),
                FileMediaTypeSingleton.getInstance().createAndReturnImageConfig());
    }

    @Test
    public void createAndReturnAudioConfig() throws Exception {
        assertNotNull(fileMediaTypeSingleton.createAndReturnAudioConfig());

        assertEquals(fileMediaTypeSingleton.createAndReturnAudioConfig().getColumns(),
                FileMediaTypeSingleton.getInstance().createAndReturnAudioConfig().getColumns());

        assertEquals(fileMediaTypeSingleton.createAndReturnAudioConfig().getOrderBy(),
                FileMediaTypeSingleton.getInstance().createAndReturnAudioConfig().getOrderBy());

        assertNotEquals(fileMediaTypeSingleton.createAndReturnAudioConfig(),
                FileMediaTypeSingleton.getInstance().createAndReturnAudioConfig());
    }

    @Test
    public void createAndReturnVideoConfig() throws Exception {
        assertNotNull(fileMediaTypeSingleton.createAndReturnVideoConfig());

        assertEquals(fileMediaTypeSingleton.createAndReturnVideoConfig().getColumns(),
                FileMediaTypeSingleton.getInstance().createAndReturnVideoConfig().getColumns());

        assertEquals(fileMediaTypeSingleton.createAndReturnVideoConfig().getOrderBy(),
                FileMediaTypeSingleton.getInstance().createAndReturnVideoConfig().getOrderBy());

        assertNotEquals(fileMediaTypeSingleton.createAndReturnVideoConfig(),
                FileMediaTypeSingleton.getInstance().createAndReturnVideoConfig());
    }
}