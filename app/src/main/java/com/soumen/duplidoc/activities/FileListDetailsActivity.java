package com.soumen.duplidoc.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soumen.duplidoc.R;
import com.soumen.duplidoc.adapters.AllDuplicatefileAdapter;
import com.soumen.duplidoc.backgroundworks.FetchFilesAsyncTask;
import com.soumen.duplidoc.callbackinterfaces.FileListRetrievalCompleteInterface;
import com.soumen.duplidoc.enums.FileType;
import com.soumen.duplidoc.extras.AppCommonValues;
import com.soumen.duplidoc.models.CommonFileModel;
import com.soumen.duplidoc.utils.HelveticaBoldTextView;
import com.soumen.duplidoc.utils.StorageHelper;

import java.util.ArrayList;

/**
 * Created by Soumen on 17-10-2017.
 */

public class FileListDetailsActivity extends AppCompatActivity implements FileListRetrievalCompleteInterface {

    private FileType fileType;
    private int READ_REQUEST_CODE = 101;
    private HelveticaBoldTextView txtMessage, txtSorry;
    private TextView txtSpace;
    private CardView cardRecoverableMemory;
    private LinearLayout linDuplicateListContainer;
    private RecyclerView rclDuplicateList;

    /* asynctask object */
    private boolean isExternalStorageAvailable;
    private FetchFilesAsyncTask mFetchFilesAsyncTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileType = (FileType) getIntent().getSerializableExtra(AppCommonValues.FILETAG);
        if (fileType != null) {
            setContentView(R.layout.activity_file_details);

            /* give a delay, so that things appear after that */
            final Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    isExternalStorageAvailable = new StorageHelper().isExternalStorageReadable();
                    firstCheckForPermission();
                }
            }, 300);
        }
    }

    private void firstCheckForPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                proceedWithNormalWorkFlowFromHere();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUEST_CODE);
            }
        } else {
            proceedWithNormalWorkFlowFromHere();
        }
    }

    private void proceedWithNormalWorkFlowFromHere() {
        getUiComponents();
        switch (fileType) {
            case TEXT:
                break;
            case IMAGE:
            case AUDIO:
            case VIDEO:
                workWithBackgroundThreadForRetrievingFiles();
                break;
        }
    }

    private void getUiComponents() {
        cardRecoverableMemory = (CardView) findViewById(R.id.cardRecoverableMemory);
        txtMessage = (HelveticaBoldTextView) findViewById(R.id.txtMessage);
        txtSorry = (HelveticaBoldTextView) findViewById(R.id.txtSorry);
        txtSpace = (TextView) findViewById(R.id.txtSpace);
        linDuplicateListContainer = (LinearLayout) findViewById(R.id.linDuplicateListContainer);
        rclDuplicateList = (RecyclerView) findViewById(R.id.rclDuplicateList);
        rclDuplicateList.setLayoutManager(new LinearLayoutManager(FileListDetailsActivity.this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            proceedWithNormalWorkFlowFromHere();
        } else {
            Toast.makeText(FileListDetailsActivity.this, "Cannot do anything without this permission", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    private void workWithBackgroundThreadForRetrievingFiles() {
        switch (fileType) {
            case IMAGE:
                mFetchFilesAsyncTask = new FetchFilesAsyncTask(FileListDetailsActivity.this, FileType.IMAGE);
                break;
            case AUDIO:
                mFetchFilesAsyncTask = new FetchFilesAsyncTask(FileListDetailsActivity.this, FileType.AUDIO);
                break;
            case VIDEO:
                mFetchFilesAsyncTask = new FetchFilesAsyncTask(FileListDetailsActivity.this, FileType.VIDEO);
                break;
            case TEXT:
                // not done till now //
                break;
        }
        mFetchFilesAsyncTask.execute();
        mFetchFilesAsyncTask.mFileListRetrievalCompleteInterface = FileListDetailsActivity.this;
    }

    /**
     * Converts byte value to megabytes
     *
     * @param bytes
     * @return
     */
    private double getMemoryInMb(long bytes) {
        double fileSizeInKB = bytes / 1024;
        return (fileSizeInKB / 1024);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.slide_down_activity);
    }

    @Override
    public void onFileListRetrievalCompleted(ArrayList<CommonFileModel> fileModels) {
        startComparingFiles(fileModels);
    }

    /* make it an independant function */
    private void startComparingFiles(ArrayList<CommonFileModel> fileModels) {
        long recoverableMemory = 0;
        ArrayList<CommonFileModel> deletableFiles = new ArrayList<>();
        if (fileModels == null || fileModels.size() == 0) {
            Toast.makeText(FileListDetailsActivity.this, "No Files Found", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            for (int i = 0; i < fileModels.size(); i++) {
                CommonFileModel i1 = fileModels.get(i);
                for (int j = i + 1; j < fileModels.size(); j++) {
                    CommonFileModel i2 = fileModels.get(j);
                    if (fileType == FileType.IMAGE) {
                        try {
                            if ((i1.getFileDisplayName().equalsIgnoreCase(i2.getFileDisplayName())) &&
                                    (i1.getFileSize() == i2.getFileSize()) &&
                                    (i1.getFileHeight() == i2.getFileHeight()) &&
                                    (i1.getFileWidth() == i2.getFileWidth()) &&
                                    !(i1.getFilePath().equalsIgnoreCase(i2.getFilePath()))) {
                                if (!deletableFiles.contains(i2)) {
                                    recoverableMemory += i2.getFileSize();
                                    deletableFiles.add(i2);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (fileType == FileType.AUDIO || fileType == FileType.VIDEO) {
                        try {
                            if ((i1.getFileDisplayName().equalsIgnoreCase(i2.getFileDisplayName()))
                                    && (i1.getFileSize() == i2.getFileSize())
                                    && !(i1.getFilePath().equalsIgnoreCase(i2.getFilePath()))) {
                                if (!deletableFiles.contains(i2)) {
                                    recoverableMemory += i2.getFileSize();
                                    deletableFiles.add(i2);
                                }
                                /*&&(i1.getFileSize() == i2.getFileSize()) &&
                                    (i1.getDuration() == i2.getDuration()) &&
                                    (i1.getAlbum().equalsIgnoreCase(i1.getAlbum())) &&
                                    (i1.getArtist().equalsIgnoreCase(i1.getArtist()))*/
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            cardRecoverableMemory.setVisibility(View.VISIBLE);
            if (recoverableMemory > 0) {
                txtMessage.setText("YAY !!!");
                txtSorry.setText("Recoverable Space");
                txtSpace.setText("Around " + String.format("%.2f", getMemoryInMb(recoverableMemory)) + "Mb");
                linDuplicateListContainer.setVisibility(View.VISIBLE);
                AllDuplicatefileAdapter allDuplicatefileAdapter = new AllDuplicatefileAdapter(this, deletableFiles);
                rclDuplicateList.setAdapter(allDuplicatefileAdapter);
            } else {
                txtMessage.setText("OHH !!!");
                txtSorry.setText("So Sorry");
                txtSpace.setText("No recoverable space is available :(");
            }
        }
    }
}