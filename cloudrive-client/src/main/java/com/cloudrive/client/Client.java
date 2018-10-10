package com.cloudrive.client;

import com.cloudrive.client.filelist.FileListItem;
import com.cloudrive.client.handlers.TestClientInboundHandler;
import com.cloudrive.client.handlers.TestClientOutboundHandler;
import com.cloudrive.common.PartOfFileMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;

public class Client implements Runnable {

    private Channel channel;
    private static Client instance = new Client();

    public Channel getChannel(){
        return channel;
    }

    public static Client getInstance(){
        return instance;
    }

    private Client(){

    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress("127.0.0.1", 7246)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            channel = socketChannel;
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(1024, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new TestClientInboundHandler(),
                                    new TestClientOutboundHandler(1),
                                    new TestClientOutboundHandler(2)
                            );
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void send(){
        System.out.println("buttonSendClicked (client)");
        channel.writeAndFlush(new PartOfFileMessage());
    }

    public void sendFileToStorage(File file) throws InterruptedException {
        Thread thread = new Thread(new FileSendThread(file, channel));
        thread.start();
        thread.join();
    }
}
