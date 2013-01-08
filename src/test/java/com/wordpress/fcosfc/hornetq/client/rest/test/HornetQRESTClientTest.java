package com.wordpress.fcosfc.hornetq.client.rest.test;

import com.wordpress.fcosfc.hornetq.client.MessagingException;
import com.wordpress.fcosfc.hornetq.client.MessagingInterface;
import com.wordpress.fcosfc.hornetq.client.rest.RESTMessagingClientFactory;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * JUnit Test Class of the HornetQ REST Interface Client
 * 
 * @author Paco Saucedo
 * @version 1.0
 * @see <a href="http://wp.me/p1zFuD-7K">Paco Saucedo's blog</a>
 */
public class HornetQRESTClientTest {

    private static final String SERVER_URL = "http://localhost:8080/HornetQRESTInterface";
    private static final String QUEUE = "jms.queue.orders";
    private static final String TOPIC = "jms.topic.orders";
    private static final String MSG_SUBSCRIBER_ID = "Test";
    
    /**
     * Default constructor
     */
    public HornetQRESTClientTest() {
    }
    
    /**
     * Test of sending a message through the REST interface to a queue and receiving it then 
     * 
     * @throws MessagingException 
     */
    @Test
    public void queueTest() throws MessagingException {
        MessagingInterface messaging;
        Order orderToSend, orderReceived;
        
        messaging = RESTMessagingClientFactory.getHornetQRESTClient(SERVER_URL, QUEUE);
        
        messaging.start();
        
        // Purge the queue
        while((orderReceived = messaging.receiveNextMessage(Order.class)) != null) {
            messaging.ackLastMessageReceived();
        }
        
        orderToSend = new Order("Test item", 5, 10.2f);        
        messaging.sendMessage(orderToSend);
        
        orderReceived = messaging.receiveNextMessage(Order.class);        
        messaging.ackLastMessageReceived();
        
        messaging.stop();
        
        assertNotNull(orderReceived);
        assertEquals(orderToSend, orderReceived);                
    }
    
    /**
     * Test of sending a message through the REST interface to a topic and receiving it then
     * 
     * @throws MessagingException 
     */
    @Test
    public void topicTest() throws MessagingException {
        MessagingInterface messaging;
        Order orderToSend, orderReceived;
        
        messaging = RESTMessagingClientFactory.getHornetQRESTClient(SERVER_URL, TOPIC, MSG_SUBSCRIBER_ID);
        
        messaging.start();
        
        // Purge the messages pending on the topic for the subscriber
        while((orderReceived = messaging.receiveNextMessage(Order.class)) != null) {
            messaging.ackLastMessageReceived();
        }
        
        orderToSend = new Order("Test item", 3, 15.7f);        
        messaging.sendMessage(orderToSend);
        
        orderReceived = messaging.receiveNextMessage(Order.class);        
        messaging.ackLastMessageReceived();
        
        messaging.stop();
        
        assertNotNull(orderReceived);
        assertEquals(orderToSend, orderReceived);                
    }
}
