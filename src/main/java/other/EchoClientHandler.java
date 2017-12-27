package other;

/**
 * @Author xiezd
 * @Description
 * @Date Created in  17:38 星期二 2017/12/26/026
 */
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EchoClientHandler extends SimpleChannelInboundHandler<String> {
    private String username;
    public EchoClientHandler(){}
    public EchoClientHandler(String username){
        this.username = username;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("【" + username+ "】加入房间 " + new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
