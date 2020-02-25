package com.janinc.pubsub;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-20
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;
import com.janinc.Table;
import com.janinc.util.TextUtil;

public class Message {
    private Channel channel;
    private DataObject data;
    private Table<? extends DataObject> target;

    public Message(Channel channel, Table<? extends DataObject> target, DataObject data) {
        this.channel = channel;
        this.data = data;
        this.target = target;
    }

    public Channel getChannel() { return channel; }
    public DataObject getData() { return data; }
    public Table<? extends DataObject> getTarget() { return target; }

    @Override
    public String toString() {
        return String.format("%s'%s'%s'%s'%s'%s'",
                TextUtil.pimpString("Your subscription to the channel ", TextUtil.LEVEL_INFO),
                TextUtil.pimpString(channel.toString(), TextUtil.LEVEL_STRESSED),
                TextUtil.pimpString(" with target ", TextUtil.LEVEL_INFO),
                TextUtil.pimpString(target == null ? "-- no target --" : target.getName(), TextUtil.LEVEL_STRESSED),
                TextUtil.pimpString(" arrived with the data ", TextUtil.LEVEL_INFO),
                data);
    } // toString
} // class Message
