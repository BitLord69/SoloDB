package com.janinc.pubsub;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-20
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.util.TextUtil;

public class Message {
    private Channel channel;
    private DataObject data;

    public Message(Channel channel, DataObject data) {
        this.channel = channel;
        this.data = data;
    }

    public Channel getChannel() { return channel; }
    public DataObject getData() { return data; }

    @Override
    public String toString() {
        return String.format("%s'%s'%s'%s'",
                TextUtil.pimpString("Your subscription to the channel ", TextUtil.LEVEL_INFO),
                TextUtil.pimpString(channel.toString(), TextUtil.LEVEL_STRESSED),
                TextUtil.pimpString(" arrived with the data ", TextUtil.LEVEL_INFO),
                data);
    } // toString
} // class Message
