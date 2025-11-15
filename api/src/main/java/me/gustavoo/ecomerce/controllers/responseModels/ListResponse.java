package me.gustavoo.ecomerce.controllers.responseModels;

import lombok.Data;

import java.util.List;

@Data
public class ListResponse<T> {
    private final List<T> data;
}
