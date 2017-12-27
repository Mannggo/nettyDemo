package other;

/**
 * @Author xiezd
 * @Description
 * @Date Created in  17:37 星期二 2017/12/26/026
 */
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class EchoClient {
    private final String host;
    private final int port;
    private final String username;
    static Scanner in = new Scanner(System.in);
    public EchoClient(String host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group) // 注册线程池
                    .channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
                    .remoteAddress(new InetSocketAddress(this.host, this.port)) // 绑定连接端口和host信息
                    .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast(new EchoClientHandler(username));
                        }
                    });
            ChannelFuture cf = b.connect().sync(); // 异步连接服务器
            while (true){
                String data = in.next();
                if(data.equals("exit")){
                    cf.channel().writeAndFlush("【" + username + "】走了 " + new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date())).addListener(ChannelFutureListener.CLOSE);
                    break;
                }
                cf.channel().writeAndFlush("【" + username + "】:" + new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date())+ "\n\t" + data);
            }
//            cf.channel().closeFuture().sync(); // 异步等待关闭连接channel
        } finally {
            group.shutdownGracefully().sync(); // 释放线程池资源
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.print("请输入用户名：");
        new EchoClient("127.0.0.1", 65535, in.next()).start(); // 连接127.0.0.1/65535，并启动
    }
}
