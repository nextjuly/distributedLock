package com.nextjuly.distributedlock.rest;

public class RestResponse<T> {
    private T data;
    private boolean rel;
    private int status;
    private String message;

    public static <T> RestResponse<T> success(T data) {
        return new RestResponse(data, true, 200, "操作成功");
    }

    public static RestResponse fail(int status, String message) {
        return new RestResponse((Object)null, false, status, message);
    }

    public T getData() {
        return this.data;
    }

    public boolean isRel() {
        return this.rel;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setRel(boolean rel) {
        this.rel = rel;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RestResponse() {
    }

    public RestResponse(T data, boolean rel, int status, String message) {
        this.data = data;
        this.rel = rel;
        this.status = status;
        this.message = message;
    }
}