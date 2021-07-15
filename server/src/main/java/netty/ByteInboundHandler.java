package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ByteInboundHandler extends ChannelInboundHandlerAdapter {
    private final ScriptEngine engine;

    public ByteInboundHandler() {
        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName("nashorn");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client accepted...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("received: {}", msg);
        ByteBuf buf = (ByteBuf) msg;

        StringBuilder s = new StringBuilder();
        while (buf.isReadable()) {
            char b = (char) buf.readByte();
            s.append(b);
        }
        log.debug("msg: {}", s);

        String expression = s.toString();
        String result = engine.eval(expression).toString() + "\r\n";

        log.debug("evaluated: {}", result);
        ByteBuf res = ctx.alloc().buffer();
        res.writeBytes(result.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(res);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client disconnected...");
    }
}
