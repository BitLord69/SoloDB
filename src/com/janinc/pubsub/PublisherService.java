package com.janinc.pubsub;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-20
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.pubsub.iface.Subscriber;
import com.janinc.pubsub.iface.AcknowledgeSubscriber;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class PublisherService {
    private Map<Channel, Set<Subscriber>> subscribersChannelMap = new HashMap<>();
    private Map<Channel, Set<AcknowledgeSubscriber>> acknowledgeSubscribersChannelMap = new HashMap<>();

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

    public void addAcknowledgeSubscriber(Channel channel, AcknowledgeSubscriber subscriber){
        if(acknowledgeSubscribersChannelMap.containsKey(channel)){
            Set<AcknowledgeSubscriber> subscribers = acknowledgeSubscribersChannelMap.get(channel);
            subscribers.add(subscriber);
            acknowledgeSubscribersChannelMap.put(channel, subscribers);
        } else {
            Set<AcknowledgeSubscriber> subscribers = new HashSet<AcknowledgeSubscriber>();
            subscribers.add(subscriber);
            acknowledgeSubscribersChannelMap.put(channel, subscribers);
        } // else
    } // addAcknowledgeSubscriber

    public void removeSubscriber(Channel channel, Subscriber subscriber){
        if (subscribersChannelMap.containsKey(channel)){
            Set<Subscriber> subscribers = subscribersChannelMap.get(channel);
            subscribers.remove(subscriber);
            subscribersChannelMap.put(channel, subscribers);
        } // if subscribersChannelMap...
    } // removeSubscriber

     public void removeAcknowledgeSubscriber(Channel channel, AcknowledgeSubscriber subscriber){
        if (acknowledgeSubscribersChannelMap.containsKey(channel)){
        Set<AcknowledgeSubscriber> subscribers = acknowledgeSubscribersChannelMap.get(channel);
        subscribers.remove(subscriber);
            acknowledgeSubscribersChannelMap.put(channel, subscribers);
    } // if acknowledgeSubscribersChannelMap...
} // removeAcknowledgeSubscriber

    public void broadcast(Message message){
        Channel channel = message.getChannel();
        Set<Subscriber> subscribersOfChannel = subscribersChannelMap.get(channel);

        if (subscribersOfChannel != null) {
            for (Subscriber subscriber : subscribersOfChannel) {
                subscriber.receiveSubscription(message);
            } // for Subscriber...
        } // if subscribersOfChannel...
    } // broadcast

    public boolean broadcastAndWait(Message message){
        Channel channel = message.getChannel();
        Set<AcknowledgeSubscriber> subscribersOfChannel = acknowledgeSubscribersChannelMap.get(channel);

        if (subscribersOfChannel != null) {
            for (AcknowledgeSubscriber subscriber : subscribersOfChannel) {
                if (!subscriber.receiveSubscriptionAndAcknowledge(message)) return false;
            } // for Subscriber...
        } // if subscribersOfChannel...

        return true;
    } // broadcastAndWait
} // class PublisherService
