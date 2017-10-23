package com.soumen.duplidoc.extras;

import android.provider.MediaStore;

/**
 * Created by Soumen on 10/22/2017.
 */

public class FileMediaTypeSingleton {

    private static FileMediaTypeSingleton fileMediaTypeSingleton = new FileMediaTypeSingleton();

    /* Image Section */
    private static String[] imageColumns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED, MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.SIZE};;
    private static String imageOrderBy = MediaStore.Images.Media._ID;

    /* Audio Section */
    private static String[] audioColumns = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATE_MODIFIED, MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM};
    private static String audioOrderBy = MediaStore.Audio.Media._ID;

    /* Video Section */
    private static String[] videoColumns = {MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED, MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.SIZE, MediaStore.Video.Media.ARTIST, MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.ALBUM};
    private static String videoOrderBy = MediaStore.Video.Media._ID;

    private FileMediaTypeSingleton() {}

    public static FileMediaTypeSingleton getInstance() {
        return fileMediaTypeSingleton;
    }

    public static MediaConfig createAndReturnImageConfig() {
        MediaConfig imageMediaConfig = new MediaConfig(imageColumns, imageOrderBy);
        return imageMediaConfig;
    }

    public static MediaConfig createAndReturnAudioConfig() {
        MediaConfig audioMediaConfig = new MediaConfig(audioColumns, audioOrderBy);
        return audioMediaConfig;
    }

    public static MediaConfig createAndReturnVideoConfig() {
        MediaConfig videoMediaConfig = new MediaConfig(videoColumns, videoOrderBy);
        return videoMediaConfig;
    }
}