package com.janinc.pubsub.iface;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-20
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.pubsub.Message;

public interface Subscriber {
    void receiveSubscription(Message message);
} // interface Subscriber
