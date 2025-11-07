package me.gustavoo.ecomerce.controllers.models;

public class ApiResponse<T> {
    public Boolean success;
    public String message;
    public T data;

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
