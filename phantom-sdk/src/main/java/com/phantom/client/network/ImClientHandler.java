package com.phantom.client.network;

import android.util.Log;

import com.phantom.client.model.NetworkMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * IM 客户端处理器
 *
 * @author Jianfeng Wang
 * @since 2019/11/1 13:06
 */
public class ImClientHandler extends ChannelInboundHandlerAdapter {

    public static String TAG = ConnectionManager.class.getSimpleName();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.i(TAG, "与接入系统建立连接：" + ctx.channel());
        ConnectionManager.getInstance().setChannel((SocketChannel) ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf byteBuf = (ByteBuf) msg;
            NetworkMessage networkMessage = NetworkMessage.parse(byteBuf);
            ConnectionManager.getInstance().onReceiveMessage(networkMessage);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.i(TAG, "连接断开：" + ctx);
        ConnectionManager.getInstance().setChannel(null);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.e(TAG, "客户端发生了异常：{}", cause);
    }
}
