package com.phantom.client;


import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.phantom.client.model.request.AuthenticateResponse;
import com.phantom.client.model.request.C2CMessageResponse;
import com.phantom.client.model.request.C2GMessageResponse;
import com.phantom.client.model.Constants;
import com.phantom.client.model.request.FetchMessageRequest;
import com.phantom.client.model.request.FetchMessageResponse;
import com.phantom.client.model.request.InformFetchMessageResponse;
import com.phantom.client.model.request.Message;
import com.phantom.client.model.request.OfflineMessage;
import com.phantom.client.utils.HttpUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 连接管理器
 *
 * @author Jianfeng Wang
 * @since 2019/11/7 18:29
 */
public class ConnectionManager {

    public static String TAG = ConnectionManager.class.getSimpleName();

    /**
     * 单例
     */
    private static ConnectionManager connectionManager = new ConnectionManager();

    /**
     * 和服务端的通道
     */
    private volatile SocketChannel channel;

    /**
     * 消息发送队列
     */
    private ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1000);

    /**
     * 线程池
     */
    private ThreadPoolExecutor threadPool = null;

    /**
     * 是否shutdown
     */
    private volatile boolean shutdown = false;
    /**
     * 事件循环处理组
     */
    private EventLoopGroup connectThreadGroup = null;

    /**
     * 当前是否已经认证通过
     */
    private volatile boolean isAuthenticate = false;

    /**
     * 消息监听器
     */
    private MessageListener messageListeners = null;

    /**
     * 认证消息
     */
    private Message authenticateMessage;

    /**
     * 获取接入服务地址的接口
     */
    private String serverApi;

    private ConnectionManager() {
    }

    public void initialize(String serverApi) {
        this.serverApi = serverApi;
        new ConnectThread().start();
        new SendMessageThread().start();
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListeners = listener;
    }

    public void setAuthenticate(boolean authenticate) {
        this.isAuthenticate = authenticate;
    }


    public static ConnectionManager getInstance() {
        return connectionManager;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
        if (channel != null) {
            Log.i(TAG, "发送认证请求...");
            channel.writeAndFlush(authenticateMessage.getBuffer());
        } else {
            isAuthenticate = false;
            Log.i(TAG, "接入系统宕机了，唤醒线程进行连接...");
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void sendMessage(Message message) {
        messages.add(message);
    }

    public void shutdown() {
        this.shutdown = true;
        if (threadPool != null) {
            this.threadPool.shutdown();
            this.threadPool = null;
        }
        if (this.connectThreadGroup != null) {
            this.connectThreadGroup.shutdownGracefully();
            this.connectThreadGroup = null;
        }
    }

    /**
     * 收到消息处理
     *
     * @param message 消息
     */
    public void onReceiveMessage(Message message) {
        if (messageListeners != null) {
            messageListeners.onMessage(message);
        }
    }

    /**
     * 抓取消息
     *
     * @param uid 用户ID
     */
    public void fetchMessage(String uid, long timestamp) {
        FetchMessageRequest request = FetchMessageRequest.newBuilder()
                .setPlatform(1)
                .setSize(10)
                .setTimestamp(timestamp)
                .setUid(uid)
                .build();
        this.sendMessage(Message.buildFetcherMessageRequest(request));
    }

    /**
     * 保存认证信息
     *
     * @param message message
     */
    public void authenticate(Message message) {
        this.authenticateMessage = message;
    }

    public void reauthenticate() {
        channel.writeAndFlush(authenticateMessage.getBuffer());
    }

    /**
     * 用于建立连接的线程
     */
    class ConnectThread extends Thread {
        @Override
        public void run() {
            while (!shutdown) {
                try {
                    Log.i(TAG, "启动连接线程.....");
                    String s = HttpUtil.get(serverApi, null);
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    if (TextUtils.isEmpty(s)) {
                        Log.i(TAG, "连接不上服务器，休眠5s再次连接");
                        Thread.sleep(5 * 1000);
                        continue;
                    }
                    String ipAndPort = jsonObject.getString("ipAndPort");
                    String ip = ipAndPort.split(":")[0];
                    int port = Integer.valueOf(ipAndPort.split(":")[1]);
                    Log.i(TAG, "开始和接入系统发起连接..:" + ipAndPort);
                    connectThreadGroup = new NioEventLoopGroup();
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(connectThreadGroup)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .option(ChannelOption.SO_KEEPALIVE, true)
                            .channel(NioSocketChannel.class)
                            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(4096,
                                            Unpooled.copiedBuffer(Constants.DELIMITER)));
                                    ch.pipeline().addLast(new ImClientHandler());
                                }
                            });
                    ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
                    channelFuture.sync();
                    synchronized (ConnectionManager.this) {
                        Log.i(TAG, "连接接入系统成功，休眠......");
                        ConnectionManager.this.wait();
                    }
                } catch (Exception e) {
                    try {
                        Log.i(TAG, "连接接入系统发生异常，休眠" + 5000 + "ms后开始重新连接");
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 用于发送消息的线程
     */
    class SendMessageThread extends Thread {
        @Override
        public void run() {
            while (!shutdown) {
                try {
                    Message msg = messages.poll(10, TimeUnit.SECONDS);
                    if (msg == null) {
                        continue;
                    }
                    while (!isAuthenticate) {
                        Thread.sleep(1000);
                    }
                    if (channel != null) {
                        Log.i(TAG, "发送消息：requestType =" + Constants.requestTypeName(msg.getRequestType()));
                        channel.writeAndFlush(msg.getBuffer());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
