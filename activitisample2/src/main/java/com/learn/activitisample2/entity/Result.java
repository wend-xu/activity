package com.learn.activitisample2.entity;

import lombok.Data;

@Data
public class Result {

    private String msg;
    private String state;
    private Object result;

    public static final String RESULT_STATE_OK="10001";
    public static final String RESULT_STATE_ERROR="10002";
    public static final String RESULT_STATE_PERMISSION_DENIED="10003";

    public Result(String msg, String state) {
        this.msg = msg;
        this.state = state;
    }

    public Result(String msg, String state, Object result) {
        this.msg = msg;
        this.state = state;
        this.result = result;
    }
}
