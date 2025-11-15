package me.gustavoo.ecomerce.controllers.responseModels;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ApiResponse<T> {
    @NonNull
    private Boolean success;
    private String message;
    private T data;

    public ApiResponse(String message) {
        this.success = false;
        this.message = message;
    }

    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
    }

    public static <T>ApiResponse successResponse(T data) {
        return new ApiResponse(data);
    }

    public static ApiResponse errorResponse(String message) {
        return new ApiResponse(message);
    }
}
