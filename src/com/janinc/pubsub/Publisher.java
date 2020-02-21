package com.janinc.pubsub;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-20
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

public interface Publisher {
    void publish(Message message);
    void subscribe(Channel channel, Subscriber subscriber);
    void unsubscribe(Channel channel, Subscriber subscriber);
} // interface Publisher
