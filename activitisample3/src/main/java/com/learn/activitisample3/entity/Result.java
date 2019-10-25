package com.learn.activitisample3.entity;

import lombok.Data;

@Data
public class Result {

    private String msg;
    private String state;
    private Object data;

    public static final String RESULT_STATE_OK="10001";
    public static final String RESULT_STATE_ERROR="10002";
    public static final String RESULT_STATE_PERMISSION_DENIED="10003";

    public Result(String state) {
        this.state = state;
    }

    public Result(String msg, String state) {
        this.msg = msg;
        this.state = state;
    }

    public Result(String msg, String state, Object data) {
        this.msg = msg;
        this.state = state;
        this.data = data;
    }

    public static Result success(){
        return  new Result("执行成功",RESULT_STATE_OK);
    }

    public static Result success(String msg){
        return  new Result(msg,RESULT_STATE_OK);
    }

    public static Result success(String msg,Object data){
        return  new Result(msg,RESULT_STATE_OK,data);
    }

    public static Result error(String msg){
        return new Result(msg,RESULT_STATE_ERROR);
    }
}
