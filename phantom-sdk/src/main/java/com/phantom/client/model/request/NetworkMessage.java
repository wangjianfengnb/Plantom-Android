package com.phantom.client.model.request;

import com.phantom.client.model.Constants;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * IM消息协议
 * 消息头长度 20
 *
 * @author Jianfeng Wang
 */
public class NetworkMessage {

    /**
     * 客户端SDK版本号
     */
    protected int appSdkVersion;
    /**
     * 消息类型：请求 / 响应
     */
    protected int messageType;
    /**
     * 请求类型
     */
    protected int requestType;
    /**
     * 请求顺序
     */
    protected int sequence;
    /**
     * 消息体长度
     */
    protected int bodyLength;
    /**
     * 消息体
     */
    protected byte[] body;

    public int getAppSdkVersion() {
        return appSdkVersion;
    }

    public void setAppSdkVersion(int appSdkVersion) {
        this.appSdkVersion = appSdkVersion;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }


    /**
     * 创建认证请求
     *
     * @param authenticateRequest 认证请求
     * @return 消息
     */
    public static NetworkMessage buildAuthenticateRequest(AuthenticateRequest authenticateRequest) {
        byte[] body = authenticateRequest.toByteArray();
        NetworkMessage networkMessage = new NetworkMessage();
        networkMessage.setAppSdkVersion(Constants.APP_SDK_VERSION_1);
        networkMessage.setMessageType(Constants.MESSAGE_TYPE_REQUEST);
        networkMessage.setRequestType(Constants.REQUEST_TYPE_AUTHENTICATE);
        networkMessage.setBodyLength(body.length);
        networkMessage.setBody(body);
        return networkMessage;
    }


    public static NetworkMessage buildC2CMessageRequest(C2CMessageRequest request) {
        byte[] body = request.toByteArray();
        NetworkMessage networkMessage = new NetworkMessage();
        networkMessage.setAppSdkVersion(Constants.APP_SDK_VERSION_1);
        networkMessage.setMessageType(Constants.MESSAGE_TYPE_REQUEST);
        networkMessage.setRequestType(Constants.REQUEST_TYPE_C2C_SEND);
        networkMessage.setBodyLength(body.length);
        networkMessage.setBody(body);
        return networkMessage;
    }


    public static NetworkMessage buildFetcherMessageRequest(FetchMessageRequest request) {
        byte[] body = request.toByteArray();
        NetworkMessage networkMessage = new NetworkMessage();
        networkMessage.setAppSdkVersion(Constants.APP_SDK_VERSION_1);
        networkMessage.setMessageType(Constants.MESSAGE_TYPE_REQUEST);
        networkMessage.setRequestType(Constants.REQUEST_TYPE_MESSAGE_FETCH);
        networkMessage.setBodyLength(body.length);
        networkMessage.setBody(body);
        return networkMessage;
    }


    public static NetworkMessage buildC2gMessageRequest(C2GMessageRequest request) {
        byte[] body = request.toByteArray();
        NetworkMessage networkMessage = new NetworkMessage();

        networkMessage.setAppSdkVersion(Constants.APP_SDK_VERSION_1);
        networkMessage.setMessageType(Constants.MESSAGE_TYPE_REQUEST);
        networkMessage.setRequestType(Constants.REQUEST_TYPE_C2G_SEND);
        networkMessage.setBodyLength(body.length);
        networkMessage.setBody(body);
        return networkMessage;
    }

    public ByteBuf getBuffer() {
        ByteBuf buffer = Unpooled.buffer(Constants.HEADER_LENGTH +
                body.length + Constants.DELIMITER.length);
        buffer.writeInt(appSdkVersion);
        buffer.writeInt(messageType);
        buffer.writeInt(requestType);
        buffer.writeInt(sequence);
        buffer.writeInt(bodyLength);
        buffer.writeBytes(body);
        buffer.writeBytes(Constants.DELIMITER);
        return buffer;
    }

    public static NetworkMessage parse(ByteBuf byteBuf) {
        NetworkMessage networkMessage = new NetworkMessage();
        networkMessage.setAppSdkVersion(byteBuf.readInt());
        networkMessage.setMessageType(byteBuf.readInt());
        networkMessage.setRequestType(byteBuf.readInt());
        networkMessage.setSequence(byteBuf.readInt());
        networkMessage.setBodyLength(byteBuf.readInt());
        networkMessage.body = new byte[networkMessage.getBodyLength()];
        byteBuf.readBytes(networkMessage.body);
        return networkMessage;
    }


}
