package com.soumen.duplidoc.backgroundworks;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.soumen.duplidoc.callbackinterfaces.FileListRetrievalCompleteInterface;
import com.soumen.duplidoc.enums.FileType;
import com.soumen.duplidoc.extras.FileMediaTypeSingleton;
import com.soumen.duplidoc.extras.MediaConfig;
import com.soumen.duplidoc.models.CommonFileModel;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Soumen on 10/22/2017.
 */

public class FetchFilesAsyncTask extends AsyncTask<String, String, ArrayList<CommonFileModel>> {

    private Context mContext;
    private FileType fileType;
    private boolean isExternalMemoryPresent = true;
    private MediaConfig mediaConfig;
    private ArrayList<CommonFileModel> fileModels;
    private Cursor interNalCursor, externalCursor;
    private ProgressDialog progressDialog;

    /* callback interface */
    public FileListRetrievalCompleteInterface mFileListRetrievalCompleteInterface = null;

    public FetchFilesAsyncTask(Context mContext, FileType fileType) {
        this.mContext = mContext;
        this.fileType = fileType;
        fileModels = new ArrayList<CommonFileModel>();
    }

    public FetchFilesAsyncTask(Context mContext, FileType fileType, boolean isExternalMemoryPresent) {
        this(mContext, fileType);
        this.isExternalMemoryPresent = isExternalMemoryPresent;
    }

    private void spawnProgressDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void dismissDialog() {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        spawnProgressDialog();
    }

    @Override
    protected ArrayList<CommonFileModel> doInBackground(String... strings) {
        switch (fileType) {
            case IMAGE:
                mediaConfig = FileMediaTypeSingleton.getInstance().createAndReturnImageConfig();
                interNalCursor = mContext.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                        mediaConfig.getColumns(), null, null, mediaConfig.getOrderBy());
                externalCursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        mediaConfig.getColumns(), null, null, mediaConfig.getOrderBy());
                createArrayListOfFileModels();
                break;
            case AUDIO:
                mediaConfig = FileMediaTypeSingleton.getInstance().createAndReturnAudioConfig();
                interNalCursor = mContext.getContentResolver().query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                        mediaConfig.getColumns(), null, null, mediaConfig.getOrderBy());
                externalCursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        mediaConfig.getColumns(), null, null, mediaConfig.getOrderBy());
                createArrayListOfFileModels();
                break;
            case VIDEO:
                mediaConfig = FileMediaTypeSingleton.getInstance().createAndReturnVideoConfig();
                interNalCursor = mContext.getContentResolver().query(MediaStore.Video.Media.INTERNAL_CONTENT_URI,
                        mediaConfig.getColumns(), null, null, mediaConfig.getOrderBy());
                externalCursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        mediaConfig.getColumns(), null, null, mediaConfig.getOrderBy());
                createArrayListOfFileModels();
                break;
            case TEXT:
                checkAllFilesToCheckIfTextType();
                break;
        }
        return fileModels;
    }

    /**
     * Internal memory is always present, so checking is only done on the fact whether the external memory is present or not
     */
    private void createArrayListOfFileModels() {
        readInternalFiles();
        readExternalFiles();
    }

    @Override
    protected void onPostExecute(ArrayList<CommonFileModel> commonFileModels) {
        super.onPostExecute(commonFileModels);
        dismissDialog();
        if (fileModels.size() != 0) {
            mFileListRetrievalCompleteInterface.onFileListRetrievalCompleted(fileModels);
        } else {
            mFileListRetrievalCompleteInterface.onFileListRetrievalCompleted(null);
        }
    }

    private void readInternalFiles() {
        for (int i = 0; i < interNalCursor.getCount(); i++) {
            interNalCursor.moveToPosition(i);
            CommonFileModel i1 = new CommonFileModel();
            i1.setFilePath(interNalCursor.getString(interNalCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            i1.setFileDisplayName(interNalCursor.getString(interNalCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
            i1.setFileAddedDate(interNalCursor.getLong(interNalCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)));
            i1.setFileModifiedDate(interNalCursor.getLong(interNalCursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)));
            if (fileType == FileType.IMAGE) {
                i1.setFileTakenDate(interNalCursor.getLong(interNalCursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)));
                i1.setFileSize(interNalCursor.getLong(interNalCursor.getColumnIndex(MediaStore.Images.Media.SIZE)));
                i1.setFileHeight(interNalCursor.getInt(interNalCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)));
                i1.setFileWidth(interNalCursor.getInt(interNalCursor.getColumnIndex(MediaStore.Images.Media.WIDTH)));
            } else if (fileType == FileType.AUDIO) {
                i1.setFileTakenDate(interNalCursor.getLong(interNalCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)));
                i1.setFileSize(interNalCursor.getLong(interNalCursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                i1.setAlbum(interNalCursor.getString(interNalCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                i1.setArtist(interNalCursor.getString(interNalCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                i1.setDuration(interNalCursor.getLong(interNalCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            } else if (fileType == FileType.VIDEO) {
                i1.setFileTakenDate(interNalCursor.getLong(interNalCursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)));
                i1.setFileSize(interNalCursor.getLong(interNalCursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
                i1.setAlbum(interNalCursor.getString(interNalCursor.getColumnIndex(MediaStore.Video.Media.ALBUM)));
                i1.setArtist(interNalCursor.getString(interNalCursor.getColumnIndex(MediaStore.Video.Media.ARTIST)));
                i1.setDuration(interNalCursor.getLong(interNalCursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
            }
            fileModels.add(i1);
            Log.e("duration", "" + i1.getDuration());
        }
        interNalCursor.close();
    }

    private void readExternalFiles() {
        for (int i = 0; i < externalCursor.getCount(); i++) {
            externalCursor.moveToPosition(i);
            CommonFileModel i1 = new CommonFileModel();
            i1.setFilePath(externalCursor.getString(externalCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            i1.setFileDisplayName(externalCursor.getString(externalCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));
            i1.setFileAddedDate(externalCursor.getLong(externalCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)));
            i1.setFileModifiedDate(externalCursor.getLong(externalCursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)));
            if (fileType == FileType.IMAGE) {
                i1.setFileTakenDate(externalCursor.getLong(externalCursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)));
                i1.setFileSize(externalCursor.getLong(externalCursor.getColumnIndex(MediaStore.Images.Media.SIZE)));
                i1.setFileHeight(externalCursor.getInt(externalCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)));
                i1.setFileWidth(externalCursor.getInt(externalCursor.getColumnIndex(MediaStore.Images.Media.WIDTH)));
            } else if (fileType == FileType.AUDIO) {
                i1.setFileTakenDate(externalCursor.getLong(externalCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)));
                i1.setFileSize(externalCursor.getLong(externalCursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                i1.setAlbum(externalCursor.getString(externalCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                i1.setArtist(externalCursor.getString(externalCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                i1.setDuration(externalCursor.getLong(externalCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            } else if (fileType == FileType.VIDEO) {
                i1.setFileTakenDate(externalCursor.getLong(externalCursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)));
                i1.setFileSize(externalCursor.getLong(externalCursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
                i1.setAlbum(externalCursor.getString(externalCursor.getColumnIndex(MediaStore.Video.Media.ALBUM)));
                i1.setArtist(externalCursor.getString(externalCursor.getColumnIndex(MediaStore.Video.Media.ARTIST)));
                i1.setDuration(externalCursor.getLong(externalCursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
            }
            fileModels.add(i1);
        }
        externalCursor.close();
    }

    private void checkAllFilesToCheckIfTextType() {
        File sdcardObj=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        listOfFile(sdcardObj);
    }

    private void listOfFile(File directory) {
        if(directory.isDirectory()){
            File[] files = directory.listFiles();
            try {
                for (File f : files){
                    if(!f.isDirectory()) {
                        if(f.getName().endsWith(".doc") || f.getName().endsWith(".txt") || f.getName().endsWith(".docx")
                                || f.getName().endsWith(".rtf") || f.getName().endsWith(".xls") || f.getName().endsWith(".xlsx")
                                || f.getName().endsWith(".ppt") || f.getName().endsWith(".pptx")) {
                            CommonFileModel c1 = new CommonFileModel();
                            c1.setFilePath(f.getAbsolutePath());
                            c1.setFileDisplayName(f.getName());
                            c1.setFileSize(f.length());
                            c1.setFileAddedDate(f.lastModified());
                            fileModels.add(c1);
                        }
                    } else {
                        this.listOfFile(f);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}