package com.excelsior.handler;


import com.excelsior.MessageUtils;
import com.excelsior.push.EnhancedNotification;
import com.excelsior.push.EnhancedNotificationError;
import com.excelsior.stats.StatsManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.ssl.SslHandler;

import java.nio.Buffer;
import java.nio.ByteBuffer;


public class EnhancedPushReceivedHandler extends SimpleChannelUpstreamHandler {

     private static final Logger log = LogManager.getLogger(EnhancedPushReceivedHandler.class);

      @Override
      public void handleUpstream(
              ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
          if (e instanceof ChannelStateEvent) {
             // log.info(e.toString());
          }
          super.handleUpstream(ctx, e);
      }

      @Override
      public void channelConnected(
              ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
          // Get the SslHandler from the pipeline
          // which were added in SecureChatPipelineFactory.
          SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);
          // Begin handshake.
          ChannelFuture handshake = sslHandler.handshake();
          handshake.addListener(new ConnectionLogger(sslHandler));
          StatsManager.incrConnectionCount();
      }

    @Override
    public void channelDisconnected(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        StatsManager.decrConnectionCount();
        super.channelDisconnected(ctx,e);
    }

      @Override
      public void messageReceived(
             ChannelHandlerContext ctx, MessageEvent e) {
          EnhancedNotification message = (EnhancedNotification) e.getMessage();
          short errorCode = MessageUtils.getErrorCode(message);
          if (errorCode != 0 ) {
              writeErrorCode(ctx.getChannel(), message,errorCode);
              //ctx.getChannel().close();
          }
          StatsManager.incr(message);
      }

      @Override
      public void exceptionCaught(
             ChannelHandlerContext ctx, ExceptionEvent e) {
          log.error("Unexpected exception from downstream.", e.getCause());
          e.getChannel().close();
          StatsManager.decrConnectionCount();
      }

    @Override
    public void writeComplete(
            ChannelHandlerContext ctx, WriteCompletionEvent e) throws Exception {
        ctx.sendUpstream(e);
    }


     private static final class ConnectionLogger implements ChannelFutureListener {

         private final SslHandler sslHandler;

         ConnectionLogger(SslHandler sslHandler) {
             this.sslHandler = sslHandler;
         }

         public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                 // Once session is secured, send a greeting.
                 log.info(future.getChannel().getRemoteAddress()+" connected with " +
                                 sslHandler.getEngine().getSession().getCipherSuite() +
                                 " cipher suite.");
             } else {
                 future.getChannel().close();
             }
         }
     }

    private void writeErrorCode(Channel channel, EnhancedNotification message, int errorCode) {
        EnhancedNotificationError error = new EnhancedNotificationError();
        error.setStatus((short) errorCode);
        error.setId(message.getId());
        ChannelFuture write = channel.write(error);
        write.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                future.getChannel().disconnect();
            }
        });
    }
}