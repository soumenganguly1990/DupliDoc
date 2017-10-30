package com.soumen.duplidoc.viewholders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.soumen.duplidoc.R;

/**
 * Created by Soumen on 17-10-2017.
 */

public class FileHolder extends RecyclerView.ViewHolder {

    public TextView txtFileName;
    public TextView txtFileSize;
    public TextView txtFileAddDate;
    public TextView txtFilePath;
    public CardView cardDuplicateItem;

    public FileHolder(View itemView) {
        super(itemView);

        txtFileName = (TextView) itemView.findViewById(R.id.txtFileName);
        txtFileSize = (TextView) itemView.findViewById(R.id.txtFileSize);
        txtFileAddDate = (TextView) itemView.findViewById(R.id.txtFileAddDate);
        txtFilePath = (TextView) itemView.findViewById(R.id.txtFilePath);
        cardDuplicateItem = (CardView) itemView.findViewById(R.id.cardDuplicateItem);
    }
}