package com.janinc.pubsub.iface;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-20
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.pubsub.Channel;
import com.janinc.pubsub.Message;

public interface Publisher {
    void publish(Message message);
    void addSubscriber(Channel channel, Subscriber subscriber);
    void removeSubscriber(Channel channel, Subscriber subscriber);
} // interface Publisher
