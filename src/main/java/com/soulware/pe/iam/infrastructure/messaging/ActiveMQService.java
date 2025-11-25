package com.soulware.pe.iam.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulware.pe.iam.interfaces.messaging.dto.RegisterAccountMessage;
import com.soulware.pe.iam.interfaces.messaging.dto.RegisterResponseDTO;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMQService {
    
    private static final String BROKER_URL = "tcp://localhost:61616"; // Default ActiveMQ URL
    private static final String IAM_REGISTER_QUEUE = "iam_register";
    private static final String APIGATEWAY_REGISTER_QUEUE = "apigateway_register";
    
    private final ConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper;
    
    public ActiveMQService() {
        this.connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        this.objectMapper = new ObjectMapper();
    }
    
    public ActiveMQService(String brokerUrl) {
        this.connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        this.objectMapper = new ObjectMapper();
    }
    
    public void sendRegisterResponse(RegisterResponseDTO response) {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            connection.start();
            
            Destination destination = session.createQueue(APIGATEWAY_REGISTER_QUEUE);
            MessageProducer producer = session.createProducer(destination);
            
            String jsonMessage = objectMapper.writeValueAsString(response);
            TextMessage message = session.createTextMessage(jsonMessage);
            
            producer.send(message);
            
            System.out.println("Sent register response to queue: " + jsonMessage);
            
        } catch (Exception e) {
            System.err.println("Error sending message to ActiveMQ: " + e.getMessage());
            throw new RuntimeException("Failed to send message to ActiveMQ", e);
        }
    }
    
    public RegisterAccountMessage receiveRegisterMessage() {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            connection.start();
            
            Destination destination = session.createQueue(IAM_REGISTER_QUEUE);
            MessageConsumer consumer = session.createConsumer(destination);
            
            // Wait for message with timeout (5 seconds)
            Message message = consumer.receive(5000);
            
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String jsonContent = textMessage.getText();
                
                System.out.println("Received register message from queue: " + jsonContent);
                
                return objectMapper.readValue(jsonContent, RegisterAccountMessage.class);
            }
            
            return null;
            
        } catch (Exception e) {
            System.err.println("Error receiving message from ActiveMQ: " + e.getMessage());
            throw new RuntimeException("Failed to receive message from ActiveMQ", e);
        }
    }
    
    public void startMessageListener(MessageListener listener) {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            connection.start();
            
            Destination destination = session.createQueue(IAM_REGISTER_QUEUE);
            MessageConsumer consumer = session.createConsumer(destination);
            
            consumer.setMessageListener(listener);
            
            System.out.println("Started listening to queue: " + IAM_REGISTER_QUEUE);
            
            // Keep the connection alive
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    consumer.close();
                    session.close();
                    connection.close();
                } catch (JMSException e) {
                    System.err.println("Error closing ActiveMQ connection: " + e.getMessage());
                }
            }));
            
        } catch (Exception e) {
            System.err.println("Error starting message listener: " + e.getMessage());
            throw new RuntimeException("Failed to start message listener", e);
        }
    }
}
