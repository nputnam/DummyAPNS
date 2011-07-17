package com.excelsior.encoder;

import com.excelsior.push.EnhancedNotificationError;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class EnhancedPushErrorEncoder extends OneToOneEncoder {
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        EnhancedNotificationError error = (EnhancedNotificationError) msg;

        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

        buffer.writeByte(8);
        buffer.writeByte(error.getStatus());
        buffer.writeInt((int) error.getId());
        return buffer;
    }
}
