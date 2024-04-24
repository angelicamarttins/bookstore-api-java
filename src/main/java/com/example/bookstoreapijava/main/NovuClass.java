package com.example.bookstoreapijava.main;

import co.novu.api.common.SubscriberRequest;
import co.novu.api.events.requests.Topic;
import co.novu.api.events.requests.TriggerEventRequest;
import co.novu.api.events.responses.TriggerEventResponse;
import co.novu.api.notifications.requests.NotificationRequest;
import co.novu.api.notifications.responses.NotificationsResponse;
import co.novu.api.subscribers.requests.UpdateSubscriberRequest;
import co.novu.common.base.Novu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NovuClass {
  public static void main(String[] args) {
    String apiKey = "70af9423233f461a0667d1967522f65b";
    Novu novu = new Novu(apiKey);

//    subscribe(novu);
    //update(novu);
    SubscriberRequest subscriberRequest = new SubscriberRequest();
    subscriberRequest.setSubscriberId("65d5eec934a25e4f83a5fb18");
    trigger(novu, subscriberRequest);
//    SingleSubscriberResponse sub =
//        novu.getSubscriber("e9b613b6-5ecd-4943-bb85-306c5534e46d");

//    TopicRequest topicRequestA = new TopicRequest();
//    TopicRequest topicRequestB = new TopicRequest();
//    topicRequestA.setKey("testA");
//    topicRequestB.setKey("testB");
//    topicRequestA.setName("This is a test A/B and this is for A group.");
//    topicRequestB.setName("This is a test A/B and this is for B group.");
//    novu.createTopic(topicRequestA);
//    novu.createTopic(topicRequestB);

//    TopicResponse a = novu.getTopic("testA");
//    TopicResponse b =novu.getTopic("testB");
//    System.out.println(a);
//    System.out.println(b);

//    List<String> subA = Arrays.asList("e9b613b6-5ecd-4943-bb85-306c5534e46d");
//    List<String> subB = Arrays.asList("9b88fe4a-ecd1-4812-ab48-c4224c2ee38a");
//    SubscriberAdditionRequest subscriberAdditionRequestA = new SubscriberAdditionRequest();
//    subscriberAdditionRequestA.setSubscribers(subA);
//    SubscriberAdditionRequest subscriberAdditionRequestB = new SubscriberAdditionRequest();
//    subscriberAdditionRequestB.setSubscribers(subB);
//    novu.addSubscriberToTopic(subscriberAdditionRequestA, "testA");
//    novu.addSubscriberToTopic(subscriberAdditionRequestB, "testB");

//    triggerA(novu);
//    triggerB(novu);
  }

  private static SubscriberRequest subscribe(Novu novu) {
    SubscriberRequest subscriberRequest = new SubscriberRequest();
    subscriberRequest.setEmail("dramavirtual@hotmail.com");
    subscriberRequest.setFirstName("Angélica");
    subscriberRequest.setLastName("Martins");
    subscriberRequest.setPhone("+5511952219666");
    subscriberRequest.setSubscriberId(UUID.randomUUID().toString());
    //"e9b613b6-5ecd-4943-bb85-306c5534e46d" marttinsangelica
    //"9b88fe4a-ecd1-4812-ab48-c4224c2ee38a" dramavirtual
    try {
      novu.createSubscriber(subscriberRequest);

      return subscriberRequest;
    } catch (Exception e) {
      System.out.println("Error Creating Subscriber" + e);
    }

    return null;
  }

  private static UpdateSubscriberRequest update(Novu novu) {
    UpdateSubscriberRequest subscriberRequest = new UpdateSubscriberRequest();
    subscriberRequest.setPhone("+5511952219666");

    try {
      novu.updateSubscriber(subscriberRequest, "e9b613b6-5ecd-4943-bb85-306c5534e46d");

      return subscriberRequest;
    } catch (Exception e) {
      System.out.println("Error Creating Subscriber" + e);
    }

    return null;
  }

  private static TriggerEventResponse trigger(Novu novu, SubscriberRequest subscriberRequest) {
    TriggerEventRequest triggerEventRequest = new TriggerEventRequest();
    triggerEventRequest.setName("payment.created.payer");
    triggerEventRequest.setTo(subscriberRequest);
    Map<String, Object> emailOverride = new HashMap<>();
    emailOverride.put("subject", "Pamonha amarela");
    emailOverride.put("senderName", "Angélica Biruleibe");

    triggerEventRequest.setPayload(Collections.singletonMap("customVariables", "Hello"));
    triggerEventRequest.setOverrides(Collections.singletonMap("email", emailOverride));
    try {
      TriggerEventResponse triggerEventResponse = novu.triggerEvent(triggerEventRequest);
      System.out.println("GETERROR = " + triggerEventResponse.getData());
      NotificationRequest notificationRequest = new NotificationRequest();
      notificationRequest.setTransactionId(triggerEventResponse.getData().getTransactionId());
      Thread.sleep(5000);

      NotificationsResponse notifications = novu.getNotifications(notificationRequest);
      System.out.println("NOTIFICATIONS = " + notifications);
      return triggerEventResponse;
    } catch (Exception e) {
      System.out.println("Error Creating Subscriber" + e);
    }

    return null;
  }

  private static TriggerEventResponse triggerA(Novu novu) {
    TriggerEventRequest triggerEventRequest = new TriggerEventRequest();
    triggerEventRequest.setName("groupA");

    Topic topicA = new Topic();
    topicA.setType("Topic");
    topicA.setTopicKey("testA");
    triggerEventRequest.setTo(topicA);

    try {
      return novu.triggerEvent(triggerEventRequest);
    } catch (Exception e) {
      System.out.println("Error Creating Subscriber" + e);
    }

    return null;
  }

  private static TriggerEventResponse triggerB(Novu novu) {
    TriggerEventRequest triggerEventRequest = new TriggerEventRequest();
    triggerEventRequest.setName("groupB");

    Topic topicB = new Topic();
    topicB.setType("Topic");
    topicB.setTopicKey("testB");
    triggerEventRequest.setTo(topicB);

    try {
      return novu.triggerEvent(triggerEventRequest);
    } catch (Exception e) {
      System.out.println("Error Creating Subscriber" + e);
    }

    return null;
  }
}
