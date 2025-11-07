package me.gustavoo.services.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UploadFilesModel {
    public ArrayList<FileModel> files;

    public UploadFilesModel(ArrayList<FileModel> files) {
        this.files = files;
    }
}
