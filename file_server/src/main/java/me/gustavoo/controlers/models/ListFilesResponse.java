package me.gustavoo.controlers.models;

import java.util.ArrayList;

public class ListFilesResponse {
    public ArrayList<ItemListResponse> items;

    public ListFilesResponse(ArrayList<ItemListResponse> items) {
        this.items = items;
    }
}
