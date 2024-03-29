package com.phantom.client.model;

/**
 * 常量表
 *
 * @author Jianfeng Wang
 */
public class Constants {

    /**
     * APP SDK版本号
     */
    public static final int APP_SDK_VERSION_1 = 1;
    /**
     * 消息类型：请求
     */
    public static final int MESSAGE_TYPE_REQUEST = 1;
    /**
     * 消息类型：响应
     */
    public static final int MESSAGE_TYPE_RESPONSE = 2;
    /**
     * 请求类型：用户认证
     */
    public static final int REQUEST_TYPE_AUTHENTICATE = 1;

    /**
     * 发送C2C消息
     */
    public static final int REQUEST_TYPE_C2C_SEND = 2;

    /**
     * 消息通知抓取
     */
    public static final int REQUEST_TYPE_INFORM_FETCH = 3;

    /**
     * 消息抓取请求
     */
    public static final int REQUEST_TYPE_MESSAGE_FETCH = 4;

    /**
     * 发送群聊消息
     */
    public static final int REQUEST_TYPE_C2G_SEND = 5;

    /**
     * 接入系统注册
     */
    public static final int REQUEST_TYPE_ACCEPTOR_REGISTER = 6;

    /**
     * 每条消息的分隔符
     */
    public static final byte[] DELIMITER = "$_".getBytes();
    /**
     * 响应状态：正常
     */
    public static final int RESPONSE_STATUS_OK = 1;

    /**
     * 响应状态：异常
     */
    public static final int RESPONSE_STATUS_ERROR = 2;

    /**
     * 消息头长度
     */
    public static final int HEADER_LENGTH = 20;

    /**
     * Android 平台
     */
    public static final int PLATFORM_ANDROID = 1;

    /**
     * IOS 平台
     */
    public static final int PLATFORM_IOS = 2;

    /**
     * Web平台
     */
    public static final int PLATFORM_WEB = 3;


    public static String requestTypeName(int type) {
        if (type == REQUEST_TYPE_AUTHENTICATE) {
            return "认证";
        }
        if (type == REQUEST_TYPE_C2C_SEND) {
            return "单聊消息";
        }
        if (type == REQUEST_TYPE_INFORM_FETCH) {
            return "通知拉取消息";
        }
        if (type == REQUEST_TYPE_MESSAGE_FETCH) {
            return "拉取消息";
        }
        if (type == REQUEST_TYPE_C2G_SEND) {
            return "群聊消息";
        }
        return "未知";
    }
}
