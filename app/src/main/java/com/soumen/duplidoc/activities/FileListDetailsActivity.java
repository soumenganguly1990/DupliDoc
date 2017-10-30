package com.soumen.duplidoc.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.soumen.duplidoc.BuildConfig;
import com.soumen.duplidoc.R;
import com.soumen.duplidoc.adapters.AllDuplicatefileAdapter;
import com.soumen.duplidoc.backgroundworks.FetchFilesAsyncTask;
import com.soumen.duplidoc.callbackinterfaces.FileListRetrievalCompleteInterface;
import com.soumen.duplidoc.enums.FileType;
import com.soumen.duplidoc.enums.SortByEnum;
import com.soumen.duplidoc.extras.AppCommonValues;
import com.soumen.duplidoc.models.CommonFileModel;
import com.soumen.duplidoc.utils.HelveticaBoldTextView;
import com.soumen.duplidoc.utils.StorageHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Soumen on 17-10-2017.
 */

public class FileListDetailsActivity extends AppCompatActivity implements FileListRetrievalCompleteInterface {

    private FileType fileType;
    private AllDuplicatefileAdapter allDuplicatefileAdapter;
    private ArrayList<CommonFileModel> deletableFiles;
    private int PERMIT_ALL = 101;
    private Toolbar toolListBar;
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
            getUiComponents();
            /* give a delay, so that things appear after that */
            final Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(this);
                    isExternalStorageAvailable = new StorageHelper().isExternalStorageReadable();
                    firstCheckForPermission();
                }
            }, 350);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuSortByNameAscending:
                sortFileList(SortByEnum.BYNAMEASCENDING);
                return true;
            case R.id.mnuSortByNameDescending:
                sortFileList(SortByEnum.BYNAMEDESCENDING);
                return true;
            case R.id.mnuSortBySizeAscending:
                sortFileList(SortByEnum.BYSIZEASCENDING);
                return true;
            case R.id.mnuSortBySizeDescending:
                sortFileList(SortByEnum.BYSIZEDESCENDING);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Check if the read storage permission is given or not
     */
    private void firstCheckForPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                proceedWithNormalWorkFlowFromHere();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMIT_ALL);
            }
        } else {
            proceedWithNormalWorkFlowFromHere();
        }
    }

    /**
     * Permission has been given, so proceed with normal workflow of getting all the files and whatever next
     *
     * NOT REALLY needed, it has become redundant
     */
    private void proceedWithNormalWorkFlowFromHere() {
        switch (fileType) {
            case TEXT:
            case IMAGE:
            case AUDIO:
            case VIDEO:
                workWithBackgroundThreadForRetrievingFiles();
                break;
        }
    }

    private void getUiComponents() {
        toolListBar = (Toolbar) findViewById(R.id.toolListBar);
        cardRecoverableMemory = (CardView) findViewById(R.id.cardRecoverableMemory);
        txtMessage = (HelveticaBoldTextView) findViewById(R.id.txtMessage);
        txtSorry = (HelveticaBoldTextView) findViewById(R.id.txtSorry);
        txtSpace = (TextView) findViewById(R.id.txtSpace);
        linDuplicateListContainer = (LinearLayout) findViewById(R.id.linDuplicateListContainer);
        rclDuplicateList = (RecyclerView) findViewById(R.id.rclDuplicateList);
        rclDuplicateList.setLayoutManager(new LinearLayoutManager(FileListDetailsActivity.this));
        setSupportActionBar(toolListBar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            proceedWithNormalWorkFlowFromHere();
        } else {
            showWhyThisPermissionIsRequired();
        }
    }

    /**
     * Fetch all the files in a background asynctask
     */
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
                mFetchFilesAsyncTask = new FetchFilesAsyncTask(FileListDetailsActivity.this, FileType.TEXT);
                break;
        }
        mFetchFilesAsyncTask.execute();
        mFetchFilesAsyncTask.mFileListRetrievalCompleteInterface = FileListDetailsActivity.this;
    }

    /**
     * Converts byte value to megabytes
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

    /**
     * Sets the value of duplicate files to the deletable file list
     * @param fileModels
     */
    public void setDuplicateFileList(ArrayList<CommonFileModel> fileModels) {
        if(fileModels != null) {
            this.deletableFiles = fileModels;
        } else {
            deletableFiles = null;
        }
    }

    /**
     * Returns the list of deletable files
     * @return
     */
    public ArrayList<CommonFileModel> getDuplicateFileList() {
        if(deletableFiles != null) {
            return deletableFiles;
        } else {
            return null;
        }
    }

    /* make it an independant function */
    private void startComparingFiles(ArrayList<CommonFileModel> fileModels) {
        long recoverableMemory = 0;
        deletableFiles = new ArrayList<>();
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
                            if ((i1.getFileDisplayName().equalsIgnoreCase(i2.getFileDisplayName()))
                                    && (i1.getFileSize() == i2.getFileSize())
                                    && (i1.getFileHeight() == i2.getFileHeight())
                                    && (i1.getFileWidth() == i2.getFileWidth())
                                    && !(i1.getFilePath().equalsIgnoreCase(i2.getFilePath()))) {
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
                                    && (i1.getDuration() == i2.getDuration())
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
                    } else {
                        try {
                            if((i1.getFileDisplayName()).equalsIgnoreCase(i2.getFileDisplayName())
                                    && (i1.getFileSize() == i2.getFileSize())
                                    && (!i1.getFilePath().equalsIgnoreCase(i2.getFilePath()))) {
                                if(!deletableFiles.contains(i2)) {
                                    recoverableMemory += i2.getFileSize();
                                    deletableFiles.add(i2);
                                }
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
                txtSpace.setText("Around " + String.format("%.3f", getMemoryInMb(recoverableMemory)) + "Mb");
                linDuplicateListContainer.setVisibility(View.VISIBLE);
                allDuplicatefileAdapter = new AllDuplicatefileAdapter(this, deletableFiles);
                rclDuplicateList.setAdapter(allDuplicatefileAdapter);
            } else {
                txtMessage.setText("OHH !!!");
                txtSorry.setText("So Sorry");
                txtSpace.setText("No recoverable space is available :(");
            }
        }
    }

    /**
     * Display an alertdialog to the user, telling him/ her why this is required
     */
    private void showWhyThisPermissionIsRequired() {
        final AlertDialog.Builder permissionDialogBuilder = new AlertDialog.Builder(FileListDetailsActivity.this);
        permissionDialogBuilder.setTitle(getResources().getString(R.string.app_name));
        permissionDialogBuilder.setMessage(getString(R.string.deletemsg));
        permissionDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                openSettingsPageForThisApp();
            }
        });
        permissionDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                onBackPressed();
            }
        });
        AlertDialog permissionDialog = permissionDialogBuilder.create();
        permissionDialog.setCancelable(false);
        permissionDialog.setCanceledOnTouchOutside(false);
        permissionDialog.show();
    }

    /**
     * Redirect the user to the settings page
     */
    private void openSettingsPageForThisApp() {
        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
    }

    /**
     * Sorts the duplicate file arraylist by different criteria
     * @param sortByEnum
     */
    private void sortFileList(SortByEnum sortByEnum) {
        if (deletableFiles.size() > 0 || deletableFiles != null) {
            switch (sortByEnum) {
                case BYNAMEASCENDING:
                    try {
                        Collections.sort(deletableFiles, new Comparator<CommonFileModel>() {
                            @Override
                            public int compare(CommonFileModel commonFileModel, CommonFileModel t1) {
                                return commonFileModel.getFileDisplayName().compareTo(t1.getFileDisplayName());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case BYNAMEDESCENDING:
                    try {
                        Collections.sort(deletableFiles, new Comparator<CommonFileModel>() {
                            @Override
                            public int compare(CommonFileModel commonFileModel, CommonFileModel t1) {
                                return t1.getFileDisplayName().compareTo(commonFileModel.getFileDisplayName());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case BYSIZEASCENDING:
                    try {
                        Collections.sort(deletableFiles, new Comparator<CommonFileModel>() {
                            @Override
                            public int compare(CommonFileModel commonFileModel, CommonFileModel t1) {
                                long t1Val = commonFileModel.getFileSize();
                                long t2Val = t1.getFileSize();
                                return (t1Val < t2Val ? -1 : (t1Val == t2Val ? 0 : 1));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case BYSIZEDESCENDING:
                    try {
                        Collections.sort(deletableFiles, new Comparator<CommonFileModel>() {
                            @Override
                            public int compare(CommonFileModel commonFileModel, CommonFileModel t1) {
                                long t1Val = commonFileModel.getFileSize();
                                long t2Val = t1.getFileSize();
                                return (t2Val < t1Val ? -1 : (t2Val == t1Val ? 0 : 1));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            try {
                allDuplicatefileAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(FileListDetailsActivity.this, "Nothing to sort", Toast.LENGTH_SHORT).show();
        }
    }
}