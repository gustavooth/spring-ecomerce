package me.gustavoo.controlers.models;

public class ItemListResponse {
    public boolean isDir;
    public String name;
    public String path;

    public ItemListResponse(boolean isDir, String name, String path) {
        this.isDir = isDir;
        this.path = path;
        this.name = name;
    }
}
