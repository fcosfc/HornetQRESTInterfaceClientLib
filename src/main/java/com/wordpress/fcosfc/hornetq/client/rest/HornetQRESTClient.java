package com.wordpress.fcosfc.hornetq.client.rest;

import com.wordpress.fcosfc.hornetq.client.MessagingException;
import com.wordpress.fcosfc.hornetq.client.MessagingInterface;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;
import org.jboss.resteasy.spi.Link;

/**
 * Client of the HornetQ REST Interface
 * 
 * @author Paco Saucedo
 * @version 1.0
 * @see <a href="http://wp.me/p1zFuD-7K">Paco Saucedo's blog</a>
 */
public class HornetQRESTClient implements MessagingInterface {

    private final static int SECONDS_WAITING = 30;
    private boolean isStarted;
    private boolean isTopic;
    private Link msgCreateLink;
    private Link nextMsgLink;
    private Link msgAcknowledgementLink;
    private Link msgPullConsumerCreationLink;
    private Link msgPullConsumerLink;
    private String serverURL;
    private String queue;
    private String user;
    private String password;
    private String msgSubscriberId;
    
    /**
     * Default constructor
     */
    protected HornetQRESTClient() {
        isStarted = false;
        initLinks();        
    }
    
    /**
     * Parametrized constructor
     * 
     * @param serverURL URL of the HornetQ REST Interface Web Application
     * @param queue Queue or topic where you want to send messages or receive from
     * @param user User
     * @param password Password
     * @param messagesSubscriberId ID of the subscriber for the case of the topics 
     */
    protected HornetQRESTClient(String serverURL,
            String queue,
            String user,
            String password,
            String messagesSubscriberId) {
        this.serverURL = serverURL;
        this.queue = queue;
        this.user = user;
        this.password = password;
        msgSubscriberId = messagesSubscriberId;

        isStarted = false;
        initLinks();
    }
    
    /**
     * Get the value of serverURL
     *
     * @return the value of serverURL
     */
    public String getServerURL() {
        return serverURL;
    }
    
    /**
     * Set the value of serverURL
     *
     * @param serverURL new value of serverURL
     */
    public void setServerURL(String serverURL) throws IllegalStateException {
        if (isStarted) {
            throw new IllegalStateException("The client is started, you must stop it before changing the property");
        }
        serverURL = serverURL;
    }
    
    /**
     * Get the value of queue
     *
     * @return the value of queue
     */
    public String getQueue() {
        return queue;
    }
    
    /**
     * Set the value of queue
     *
     * @param queue new value of queue
     */
    public void setQueue(String queue) throws IllegalStateException {
        if (isStarted) {
            throw new IllegalStateException("The client is started, you must stop it before changing the property");
        }
        queue = queue;
    }
    
    /**
     * Get the value of user
     *
     * @return the value of user
     */
    public String getUser() {
        return user;
    }
    
    /**
     * Set the value of user
     *
     * @param user new value of user
     */
    public void setUser(String user) throws IllegalStateException {
        if (isStarted) {
            throw new IllegalStateException("The client is started, you must stop it before changing the property");
        }
        user = user;
    }
    
    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    public void setPassword(String password) throws IllegalStateException {
        if (isStarted) {
            throw new IllegalStateException("The client is started, you must stop it before changing the property");
        }
        password = password;
    }
    
    /**
     * Get the value of msgSubscriberId
     *
     * @return the value of msgSubscriberId
     */
    public String getMsgSubscriberId() {
        return msgSubscriberId;
    }
    
    /**
     * Set the value of msgSubscriberId
     *
     * @param messagesSubscriberId new value of msgSubscriberId
     */
    public void setMsgSubscriberId(String messagesSubscriberId) throws IllegalStateException {
        if (isStarted) {
            throw new IllegalStateException("The client is started, you must stop it before changing the property");
        }
        msgSubscriberId = messagesSubscriberId;
    }

    /**
     * Method that setup the environment and allows to start to send and receive messages
     * 
     * @throws MessagingException 
     */
    @Override
    public void start() throws MessagingException {
        ClientRequest request = null;
        ClientResponse response = null;
        ClientExecutor clientExecutor = null;

        try {
            if (isStarted) {
                throw new IllegalStateException("The client is already started");
            }

            if (serverURL == null || queue == null) {
                throw new IllegalStateException("You must assign the URL of the server and the queue");
            }

            isTopic = queue.startsWith("jms.topic");

            if (isTopic) {
                if (msgSubscriberId == null) {
                    throw new MessagingException("You must assign the id of the subscriber for a topic");
                }
            }

            if ((user == null && password != null) || (user != null && password == null)) {
                throw new MessagingException("You must assign both the user and the password for authentication");
            } else if (user != null && password != null) {
                Credentials credentials;
                HttpClient httpClient;

                credentials = new UsernamePasswordCredentials(user, password);
                httpClient = new HttpClient();
                httpClient.getState().setCredentials(AuthScope.ANY, credentials);
                httpClient.getParams().setAuthenticationPreemptive(true);
                clientExecutor = new ApacheHttpClientExecutor(httpClient);
            }

            if (clientExecutor == null) {
                request = new ClientRequest(serverURL + (isTopic ? "/topics/" : "/queues/") + queue);
            } else {
                request = new ClientRequest(serverURL + (isTopic ? "/topics/" : "/queues/") + queue, clientExecutor);
            }

            response = request.head();
            if (response.getResponseStatus().equals(Response.Status.OK)) {
                msgCreateLink = response.getHeaderAsLink("msg-create");                
                msgPullConsumerCreationLink = response.getHeaderAsLink(isTopic ? "msg-pull-subscriptions" : "msg-pull-consumers");
            } else if (response.getResponseStatus().equals(Response.Status.NOT_FOUND)) {
                throw new MessagingException("The queue " + queue + " is not registered");
            } else if (response.getResponseStatus().equals(Response.Status.UNAUTHORIZED)) {
                throw new MessagingException("Not authorized access");
            } else {
                throw new MessagingException("Response code  " + response.getStatus() + " not supported");
            }

            isStarted = true;
        } catch (MessagingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MessagingException(ex);
        }
    }

    /**
     * Method for sending messages
     * 
     * @param <T> Class susceptible to be marshalled with JAXB
     * @param message Message
     * @throws MessagingException 
     */
    @Override
    public <T> void sendMessage(T message) throws MessagingException {
        ClientResponse response;

        try {
            if (!isStarted) {
                throw new IllegalStateException("The client is not started");
            }

            response = msgCreateLink.request().body(MediaType.APPLICATION_XML, message).post();

            // Redirect received, try again
            if (response.getStatus() == 307) {
                msgCreateLink = response.getLocation();
                
                response = msgCreateLink.request().body(MediaType.APPLICATION_XML, message).post();
            }
            
            if (response.getResponseStatus().equals(Response.Status.CREATED)) {
                msgCreateLink = response.getHeaderAsLink("msg-create-next");
            } else {
                throw new MessagingException("Response code  " + response.getStatus() + " not supported");
            }
        } catch (MessagingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MessagingException(ex);
        }
    }

    /**
     * Method for receiving messages
     * 
     * @param <T> Class susceptible to be marshalled with JAXB
     * @param type Class susceptible to be marshalled with JAXB
     * @return Message, null if no messages available
     * @throws MessagingException 
     */
    @Override
    public <T> T receiveNextMessage(Class<T> type) throws MessagingException {
        T message = null;
        ClientResponse<T> response;

        try {
            if (!isStarted) {
                throw new IllegalStateException("The client is not started");
            }

            if (msgAcknowledgementLink != null) {
                throw new IllegalStateException("There is a message waiting for acknowledgement");
            }

            // Creates the consumer if needed
            if (msgPullConsumerLink == null) {
                if (isTopic) {
                    response = msgPullConsumerCreationLink.request()
                            .formParameter("autoAck", "false")
                            .formParameter("durable", "true")
                            .formParameter("name", msgSubscriberId)
                            .post();
                } else {
                    response = msgPullConsumerCreationLink.request().formParameter("autoAck", "false").post();
                }
                
                if (response.getResponseStatus().equals(Response.Status.CREATED)) {
                    msgPullConsumerLink = response.getLocation();                    
                    nextMsgLink = response.getHeaderAsLink("msg-acknowledge-next");
                } else if (response.getResponseStatus().equals(Response.Status.NO_CONTENT)) {
                    nextMsgLink = response.getHeaderAsLink("msg-acknowledge-next");              
                } else {
                    throw new MessagingException("Response code  " + response.getStatus() + " not supported");
                }
            }

            response = nextMsgLink.request()
                    .header("Accept-Wait", SECONDS_WAITING)
                    .header("Accept", MediaType.APPLICATION_XML)
                    .post();

            if (response.getResponseStatus().equals(Response.Status.OK)) {
                message = (T) response.getEntity(type);
                msgAcknowledgementLink = response.getHeaderAsLink("msg-acknowledgement");
            } else if (response.getResponseStatus().equals(Response.Status.SERVICE_UNAVAILABLE)) {
                nextMsgLink = response.getHeaderAsLink("msg-acknowledge-next");
            } else if (response.getResponseStatus().equals(Response.Status.PRECONDITION_FAILED)) {
                nextMsgLink = response.getHeaderAsLink("msg-acknowledge-next");
            } else {
                throw new MessagingException("Response code  " + response.getStatus() + " not supported");
            }
        } catch (MessagingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MessagingException(ex);
        }

        return message;
    }

    /**
     * Acknowledges the last message received
     * 
     * @throws MessagingException 
     */
    @Override
    public void ackLastMessageReceived() throws MessagingException {
        ClientResponse response;

        try {
            if (!isStarted) {
                throw new IllegalStateException("The client is not started");
            }

            if (msgAcknowledgementLink == null) {
                throw new MessagingException("No messages waiting for acknowledgement");
            }

            response = msgAcknowledgementLink.request().formParameter("acknowledge", "true").post();

            if (response.getResponseStatus().equals(Response.Status.NO_CONTENT)) {
                nextMsgLink = response.getHeaderAsLink("msg-acknowledge-next");                
                msgAcknowledgementLink = null;
            } else {
                throw new MessagingException("Response code  " + response.getStatus() + " not supported");
            }
        } catch (MessagingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MessagingException(ex);
        }
    }

    /**
     * Free the resources, stop from sending and receiving messages
     * 
     * @throws MessagingException 
     */
    @Override
    public void stop() throws MessagingException {
        ClientResponse response;

        try {
            if (!isStarted) {
                throw new IllegalStateException("The client is not started");
            }

            if (msgPullConsumerLink != null && (!isTopic)) {
                response = msgPullConsumerLink.request().delete();

                if (!response.getResponseStatus().equals(Response.Status.NO_CONTENT)) {
                    throw new MessagingException("Response code  " + response.getStatus() + " not supported");
                }
            }

            isStarted = false;
            initLinks();
        } catch (MessagingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MessagingException(ex);
        }
    }
    
    /**
     * Initialization of the the auxiliary links
     */
    private void initLinks() {
        msgAcknowledgementLink = null;
        msgPullConsumerLink = null;
        msgPullConsumerCreationLink = null;
        msgCreateLink = null;
        nextMsgLink = null;
    }
}
