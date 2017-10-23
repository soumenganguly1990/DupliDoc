package com.soumen.duplidoc.callbackinterfaces;

import com.soumen.duplidoc.models.CommonFileModel;
import java.util.ArrayList;

/**
 * Created by Soumen on 23-10-2017.
 */

public interface FileListRetrievalCompleteInterface {
    public void onFileListRetrievalCompleted(ArrayList<CommonFileModel> fileModels);
}