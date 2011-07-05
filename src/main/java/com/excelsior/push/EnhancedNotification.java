package com.excelsior.push;

/**
 * Class to represent a enhanced push notification as outlined by Apple here :
 *
 * http://developer.apple.com/library/ios/#documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CommunicatingWIthAPS/CommunicatingWIthAPS.html
 *
 *
 */
public class EnhancedNotification {

    public enum State {
        READ_COMMAND,
        READ_ID,
        READ_EXPIRY,
        READ_TOKEN_LENGTH,
        READ_TOKEN,
        READ_PAYLOAD_LENGTH,
        READ_PAYLOAD,
        DONE
    }

    private short command;
    private long id;
    private long expiry;
    private short tokenLength;
    private byte[] token;
    private short payloadLength;
    private byte[] payload;
    private State state;

    public EnhancedNotification() {
        state = EnhancedNotification.State.READ_COMMAND;
    }

    public short getCommand() {
        return command;
    }

    public void setCommand(short command) {
        this.command = command;
        state = EnhancedNotification.State.READ_ID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        state = EnhancedNotification.State.READ_EXPIRY;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
        state = EnhancedNotification.State.READ_TOKEN_LENGTH;
    }

    public short getTokenLength() {
        return tokenLength;
    }

    public void setTokenLength(short tokenLength) {
        this.tokenLength = tokenLength;
        state = EnhancedNotification.State.READ_TOKEN;
    }

    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = token;
        state = EnhancedNotification.State.READ_PAYLOAD_LENGTH;
    }

    public short getPayloadLength() {
        return payloadLength;
    }

    public void setPayloadLength(short payloadLength) {
        this.payloadLength = payloadLength;
        state = EnhancedNotification.State.READ_PAYLOAD;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
        state = EnhancedNotification.State.DONE;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
