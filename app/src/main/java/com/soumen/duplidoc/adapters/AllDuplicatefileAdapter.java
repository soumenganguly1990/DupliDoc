package com.soumen.duplidoc.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.soumen.duplidoc.R;
import com.soumen.duplidoc.activities.FileListDetailsActivity;
import com.soumen.duplidoc.models.CommonFileModel;
import com.soumen.duplidoc.viewholders.FileHolder;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Soumen on 17-10-2017.
 */

public class AllDuplicatefileAdapter extends RecyclerView.Adapter<FileHolder> {

    private int lastPosition = -1;
    Context mContext;
    ArrayList<CommonFileModel> fileList;

    public AllDuplicatefileAdapter(Context mContext, ArrayList<CommonFileModel> fileList) {
        this.mContext = mContext;
        this.fileList = fileList;
    }

    @Override
    public FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_duplicate_items, parent, false);
        FileHolder fh = new FileHolder(v);
        return fh;
    }

    @Override
    public void onBindViewHolder(FileHolder holder, final int position) {
        holder.txtFileName.setText(fileList.get(position).getFileDisplayName());
        holder.txtFileSize.setText("Size    " + String.format("%.3f", getMemoryInMb(fileList.get(position).getFileSize())) + "Mb");
        holder.txtFileAddDate.setText("Added On    " + formattedDate(fileList.get(position).getFileAddedDate()));
        holder.txtFilePath.setText("Path    " + fileList.get(position).getFilePath());

        /* set a deletion option for a long click on the card */
        holder.cardDuplicateItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setCancelable(true)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                /* okay, this is a file, so we need to delete it */
                                try {
                                    Log.e("file path", fileList.get(position).getFilePath());
                                    File f = new File(fileList.get(position).getFilePath());
                                    if(f.exists()) {
                                        if (f.delete()) {
                                            fileList.remove(position);
                                            notifyDataSetChanged();
                                            Toast.makeText(mContext, "File deleted successfully", Toast.LENGTH_SHORT).show();
                                            callBroadCast();
                                        } else {
                                            Toast.makeText(mContext, "File could not be deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.e("file not found", "File was not found");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setTitle(mContext.getString(R.string.app_name))
                        .setMessage("Would you like to delete this file? It will be permanently lost in that case")
                        .show();
                return true;
            }
        });
        setAnimation(holder.itemView, position);
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            MediaScannerConnection.scanFile(mContext, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        } else {
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    @Override
    public int getItemCount() {
        return (null != fileList ? fileList.size() : 0);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if(position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.4f, 1.0f, 0.4f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(200);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    private String formattedDate(long creationTimeStamp) {
        Date date = new Date(creationTimeStamp * 1000L);
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        return format.format(date);
    }

    private double getMemoryInMb(long bytes) {
        double fileSizeInKB = bytes / 1024;
        return (fileSizeInKB / 1024);
    }
}