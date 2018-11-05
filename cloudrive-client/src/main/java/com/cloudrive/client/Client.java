package com.cloudrive.client;

import com.cloudrive.client.handlers.ClientInboundHandler;
import com.cloudrive.common.AuthMessage;
import com.cloudrive.common.Command;
import com.cloudrive.common.TransferCommandType;
import com.cloudrive.common.handlers.FilterHandlerOutbound;
import com.cloudrive.common.interfaces.TransferCommon;
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

    public mainController getMainController() {
        return mainController;
    }

    public void setMainController(mainController mainController) {
        this.mainController = mainController;
    }

    private mainController mainController;

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
                                    new ObjectDecoder(1024*1024*10, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new FilterHandlerOutbound(),
                                    new ClientInboundHandler()
                            );
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect().sync();
            sendAuthMessage(AppSettings.getInstance().getEmail(), AppSettings.getInstance().getPassword());
            getDirList(null);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("Client connection closing...");
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Отправить файл по каналу
     * @param file Отправляемый файл
     * @throws InterruptedException
     */
    public void sendFileToStorage(File file) {
        Thread thread = new FileSendThread(file, channel);
        thread.setDaemon(true);
        thread.start();
    }

    public void getDirList(String path) throws InterruptedException {
        Thread thread = new Thread(() -> {
            channel.writeAndFlush(new Command(TransferCommandType.GETDIRLIST, path));
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void sendObject(TransferCommon obj){
        channel.writeAndFlush(obj);
    }

    public void sendAuthMessage(String email, String password){
        channel.writeAndFlush(new AuthMessage(email, password));
    }

}
