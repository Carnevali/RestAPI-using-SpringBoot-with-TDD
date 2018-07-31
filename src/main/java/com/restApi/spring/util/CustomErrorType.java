package com.restApi.spring.util;

/**
 * Created by felipecarnevalli on 14/7/18.
 */
public class CustomErrorType {
    private String errorMessage;

    public CustomErrorType(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
