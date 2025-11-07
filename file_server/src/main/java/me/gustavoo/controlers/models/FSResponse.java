package me.gustavoo.controlers.models;

public class FSResponse<T> {
    public Boolean success;
    public String message;
    public T data;

    public FSResponse(String message) {
        this.message = message;
        success = false;
    }

    public FSResponse(T data) {
        this.data = data;
        success = true;
    }

    public static <T> FSResponse success(T data) {
        return new FSResponse(data);
    }

    public static FSResponse fail(String message) {
        return new FSResponse(message);
    }
}
