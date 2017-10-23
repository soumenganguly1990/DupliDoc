package com.soumen.duplidoc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.soumen.duplidoc.R;
import com.soumen.duplidoc.models.CommonFileModel;
import com.soumen.duplidoc.viewholders.FileHolder;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Soumen on 17-10-2017.
 */

public class AllDuplicatefileAdapter extends RecyclerView.Adapter<FileHolder> {

    private int lastPosition = -1;
    Context mContext;
    ArrayList<CommonFileModel> imageList;

    public AllDuplicatefileAdapter(Context mContext, ArrayList<CommonFileModel> imageList) {
        this.mContext = mContext;
        this.imageList = imageList;
    }

    @Override
    public FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_duplicate_items, parent, false);
        FileHolder fh = new FileHolder(v);
        return fh;
    }

    @Override
    public void onBindViewHolder(FileHolder holder, int position) {
        holder.txtFileName.setText(imageList.get(position).getFileDisplayName());
        holder.txtFileSize.setText("Size " + String.format("%.2f", getMemoryInMb(imageList.get(position).getFileSize())) + "Mb");
        holder.txtFileAddDate.setText("Added On " + formattedDate(Long.parseLong(imageList.get(position).getFileAddedDate())));

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return (null != imageList ? imageList.size() : 0);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if(position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(200);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    private String formattedDate(long creationTimeStamp) {
        Date date = new Date(creationTimeStamp);
        Format format = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
        return format.format(date);
    }

    private double getMemoryInMb(long bytes) {
        double fileSizeInKB = bytes / 1024;
        return (fileSizeInKB / 1024);
    }
}
