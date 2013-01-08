package com.wordpress.fcosfc.hornetq.client;

/**
 * Messaging Exception
 * 
 * @author Paco Saucedo
 * @version 1.0
 * @see <a href="http://wp.me/p1zFuD-7K">Paco Saucedo's blog</a>
 */
public class MessagingException extends Exception {

    public MessagingException(String message) {
        super(message);
    }

    public MessagingException(Throwable t) {
        super(t);
    }
}
