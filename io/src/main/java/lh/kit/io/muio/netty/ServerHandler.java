package lh.kit.io.muio.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import lh.kit.io.utils.Calculator;

/**
 * Handles a server-side channel.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 收到客户端消息
	 * @throws UnsupportedEncodingException
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
		ByteBuf in = (ByteBuf) msg;
		byte[] req = new byte[in.readableBytes()];
		in.readBytes(req);
		String body = new String(req, StandardCharsets.UTF_8);
		System.out.println("收到客户端消息:"+body);
		String callResult = null;
		try{
			callResult = Calculator.Instance.cal(body).toString();
		}catch(Exception e){
			callResult = "错误的表达式：" + e.getMessage();
		}
		ctx.write(Unpooled.copiedBuffer(callResult.getBytes()));
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	/**
	 * 异常处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}