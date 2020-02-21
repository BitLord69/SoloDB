package com.janinc.pubsub;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-20
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PublisherService {
    private Map<Channel, Set<Subscriber>> subscribersChannelMap = new HashMap<>();

    public void addSubscriber(Channel channel, Subscriber subscriber){

        if(subscribersChannelMap.containsKey(channel)){
            Set<Subscriber> subscribers = subscribersChannelMap.get(channel);
            subscribers.add(subscriber);
            subscribersChannelMap.put(channel, subscribers);
        } else {
            Set<Subscriber> subscribers = new HashSet<Subscriber>();
            subscribers.add(subscriber);
            subscribersChannelMap.put(channel, subscribers);
        } // else
    } // addSubscriber

    public void removeSubscriber(Channel channel, Subscriber subscriber){
        if (subscribersChannelMap.containsKey(channel)){
            Set<Subscriber> subscribers = subscribersChannelMap.get(channel);
            subscribers.remove(subscriber);
            subscribersChannelMap.put(channel, subscribers);
        } // if subscribersChannelMap...
    } // removeSubscriber

    public void broadcast(Message message){
        Channel channel = message.getChannel();
        Set<Subscriber> subscribersOfChannel = subscribersChannelMap.get(channel);

        if (subscribersOfChannel != null) {
            for (Subscriber subscriber : subscribersOfChannel) {
                subscriber.update(message);
            } // for Subscriber...
        } // if subscribersOfChannel...
    } // broadcast
} // class PublisherService
