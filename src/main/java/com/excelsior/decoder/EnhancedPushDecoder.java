package com.excelsior.decoder;

import com.excelsior.push.EnhancedNotification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import static com.excelsior.push.EnhancedNotification.State.*;

public class EnhancedPushDecoder extends FrameDecoder {

    private static final Logger log = LogManager.getLogger(EnhancedPushDecoder.class);

    @Override
    protected Object decode(ChannelHandlerContext channelHandlerContext, Channel channel, ChannelBuffer channelBuffer) throws Exception {

        EnhancedNotification notification = null;

        if (channelHandlerContext.getAttachment() == null) {
            notification = new EnhancedNotification();
            channelHandlerContext.setAttachment(notification);
        } else {
            notification = (EnhancedNotification) channelHandlerContext.getAttachment();
           if (notification.getState() == DONE) {
               notification.setState(READ_COMMAND);
           }
        }


        if (notification.getState() == READ_COMMAND) {
            if (channelBuffer.readableBytes() < 1) {
                return null;
            } else {
                short command = channelBuffer.readUnsignedByte();
                notification.setCommand(command);
            }
        }

        if (notification.getState() == READ_ID) {
            if (channelBuffer.readableBytes() < 4) {
                return null;
            } else {
                long id = channelBuffer.readUnsignedInt();
                notification.setId(id);
            }
        }

        if (notification.getState() == READ_EXPIRY) {
            if (channelBuffer.readableBytes() < 4) {
                return null;
            } else {
                long expire = channelBuffer.readUnsignedInt();
                notification.setExpiry(expire);
            }
        }

        if (notification.getState() == READ_TOKEN_LENGTH) {
            if (channelBuffer.readableBytes() < 2) {
                return null;
            } else {
                int tokenLength = channelBuffer.readUnsignedShort();
                notification.setTokenLength((short) tokenLength);
            }
        }

        if (notification.getState() == READ_TOKEN) {
            if (channelBuffer.readableBytes() < notification.getTokenLength()) {
                return null;
            } else {
                byte[] token = new byte[notification.getTokenLength()];
                channelBuffer.readBytes(token, 0, notification.getTokenLength());
                notification.setToken(token);
            }
        }

        if (notification.getState() == READ_PAYLOAD_LENGTH) {
            if (channelBuffer.readableBytes() < 2) {
                return null;
            } else {
                int payloadLength = channelBuffer.readUnsignedShort();
                notification.setPayloadLength((short) payloadLength);
            }
        }
        if (notification.getState() == READ_PAYLOAD) {
            if (channelBuffer.readableBytes() < notification.getPayloadLength()) {
                return null;
            } else {
                byte[] payload = new byte[notification.getPayloadLength()];
                channelBuffer.readBytes(payload, 0, notification.getPayloadLength());
                notification.setPayload(payload);
            }
        }

        channelBuffer.markReaderIndex();

        if (notification.getState() == DONE) {

            return notification;
        } else {
            return null;
        }
    }
}
