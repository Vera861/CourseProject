 package netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        log.debug("received: {}", s);
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
        String dateS = format.format(date);
        ctx.writeAndFlush(dateS + " " + s);
    }
}
