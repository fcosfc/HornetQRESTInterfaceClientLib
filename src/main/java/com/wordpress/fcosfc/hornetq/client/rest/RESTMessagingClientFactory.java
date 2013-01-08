package com.wordpress.fcosfc.hornetq.client.rest;

import com.wordpress.fcosfc.hornetq.client.MessagingException;
import com.wordpress.fcosfc.hornetq.client.MessagingInterface;

/**
 * Client Factory
 * 
 * @author Paco Saucedo
 * @version 1.0
 * @see <a href="http://wp.me/p1zFuD-7K">Paco Saucedo's blog</a>
 */
public class RESTMessagingClientFactory {

    /**
     * Factory method
     * 
     * @param serverURL URL of the HornetQ REST Interface Web Application
     * @param queue Queue where you want to send messages or receive from
     * @return Client of the HornetQ REST Interface
     * @throws MessagingException 
     */
    public static MessagingInterface getHornetQRESTClient(String serverURL,
            String queue) throws MessagingException {
        HornetQRESTClient hornetQRESTClient;

        hornetQRESTClient = new HornetQRESTClient(serverURL,
                queue,
                null,
                null,
                null);

        return hornetQRESTClient;
    }

    /**
     * Factory method
     * 
     * @param serverURL URL of the HornetQ REST Interface Web Application
     * @param topic Topic where you want to send messages or receive from
     * @param msgSubscriberId ID of the subscriber for the case of the topics 
     * @return Client of the HornetQ REST Interface
     * @throws MessagingException 
     */
    public static MessagingInterface getHornetQRESTClient(String serverURL,
            String topic,
            String msgSubscriberId) throws MessagingException {
        HornetQRESTClient hornetQRESTClient;

        hornetQRESTClient = new HornetQRESTClient(serverURL,
                topic,
                null,
                null,
                msgSubscriberId);

        return hornetQRESTClient;
    }

    /**
     * Factory method
     * 
     * @param serverURL URL of the HornetQ REST Interface Web Application
     * @param queue Queue where you want to send messages or receive from
     * @param user User
     * @param password Password
     * @return Client of the HornetQ REST Interface
     * @throws MessagingException 
     */
    public static MessagingInterface getHornetQRESTClient(String serverURL,
            String queue,
            String user,
            String password) throws MessagingException {
        HornetQRESTClient hornetQRESTClient;

        hornetQRESTClient = new HornetQRESTClient(serverURL,
                queue,
                user,
                password,
                null);

        return hornetQRESTClient;
    }

    /**
     * Factory method
     * 
     * @param serverURL URL of the HornetQ REST Interface Web Application
     * @param topic Topic where you want to send messages or receive from
     * @param user User
     * @param password Password
     * @param msgSubscriberId ID of the subscriber for the case of the topics
     * @return Client of the HornetQ REST Interface
     * @throws MessagingException 
     */
    public static MessagingInterface getHornetQRESTClient(String serverURL,
            String topic,
            String user,
            String password,
            String msgSubscriberId) throws MessagingException {
        HornetQRESTClient hornetQRESTClient;

        hornetQRESTClient = new HornetQRESTClient(serverURL,
                topic,
                user,
                password,
                msgSubscriberId);

        return hornetQRESTClient;
    }
    
}
