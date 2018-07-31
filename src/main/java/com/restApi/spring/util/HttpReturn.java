package com.restApi.spring.util;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

public class HttpReturn {
    private String message;

    private Boolean success;

    public HttpReturn(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
