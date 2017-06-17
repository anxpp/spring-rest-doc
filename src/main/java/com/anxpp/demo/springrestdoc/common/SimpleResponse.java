package com.anxpp.demo.springrestdoc.common;

/**
 * 响应消息
 * Created by yangtao on 2017/6/15.
 */
public class SimpleResponse {

    public static final int SUCCESS = 1;
    public static final int FAIL = 0;

    //状态
    private int state = SUCCESS;
    //数据
    private Object data;
    //消息
    private String msg;

    private SimpleResponse(int state, Object data, String msg) {
        this.state = state;
        this.data = data;
        this.msg = msg;
    }

    public static SimpleResponse create() {
        return new SimpleResponse(SUCCESS, null, null);
    }

    public static SimpleResponse create(boolean state) {
        return new SimpleResponse(state ? SUCCESS : FAIL, null, null);
    }

    public static SimpleResponse create(Object data) {
        return new SimpleResponse(data == null ? FAIL : SUCCESS, data, null);
    }

    public static SimpleResponse create(boolean state, Object data) {
        return new SimpleResponse(state ? SUCCESS : FAIL, data, null);
    }

    public static SimpleResponse create(boolean state, Object data, String msg) {
        return new SimpleResponse(state ? SUCCESS : FAIL, data, msg);
    }

    public static SimpleResponse create(int state, Object data, String msg) {
        return new SimpleResponse(state, data, msg);
    }

    public int getState() {
        return state;
    }

    public Object getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }
}
