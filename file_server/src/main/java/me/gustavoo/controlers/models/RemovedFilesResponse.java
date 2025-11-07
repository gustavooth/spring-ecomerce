package me.gustavoo.controlers.models;

import java.util.ArrayList;

public class RemovedFilesResponse {
    public ArrayList<String> removedFiles;

    public RemovedFilesResponse(ArrayList<String> removedFiles) {
        this.removedFiles = removedFiles;
    }
}
