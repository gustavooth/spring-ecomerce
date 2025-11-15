package me.gustavoo.controlers.models;

import java.util.ArrayList;

public class ListItemsResponse {
    public ArrayList<ItemListResponse> items;

    public ListItemsResponse(ArrayList<ItemListResponse> items) {
        this.items = items;
    }
}
