package com.wordpress.fcosfc.hornetq.client;

/**
 * A simple messaging interface
 * 
 * @author Paco Saucedo
 * @version 1.0
 * @see <a href="http://wp.me/p1zFuD-7K">Paco Saucedo's blog</a>
 */
public interface MessagingInterface {

    /**
     * After calling this method, you can start to send and receive messages
     * 
     * @throws MessagingException 
     */
    void start() throws MessagingException;

    /**
     * Method that sends a message
     * 
     * @param <T> Class susceptible to be marshalled with JAXB
     * @param message Message
     * @throws MessagingException 
     */
    <T> void sendMessage(T message) throws MessagingException;

    /**
     * Method that receive a message
     * 
     * @param <T> Class susceptible to be marshalled with JAXB
     * @param type Class susceptible to be marshalled with JAXB
     * @return Message, null if no messages available
     * @throws MessagingException 
     */
    <T> T receiveNextMessage(Class<T> type) throws MessagingException;

    /**
     * Acknowledges the last message received
     * 
     * @throws MessagingException 
     */
    void ackLastMessageReceived() throws MessagingException;

    /**
     * After calling this method, you stop from sending and receiving messages
     * 
     * @throws MessagingException 
     */
    void stop() throws MessagingException;
}
