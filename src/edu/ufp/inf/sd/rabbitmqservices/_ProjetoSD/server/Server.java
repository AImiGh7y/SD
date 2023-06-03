package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.server;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.*;
import edu.ufp.inf.sd.rabbitmqservices.util.RabbitUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    //Reference for gui
    private ObserverServer gui;

    //Preferences for exchange...
    private Channel channelToRabbitMq;
    private String exchangeName;
    private BuiltinExchangeType exchangeType;
    private final String exchangeBindingKeys;
    private String messageFormat;
    //Store received message to be get by gui
    private String receivedMessage;

    private HashMap<String, Integer> waitingPlayers = new HashMap<>();


    public Server(ObserverServer gui, String host, int port, String user, String pass, String exchangeName, BuiltinExchangeType exchangeType, String messageFormat, String bindingKeys) throws IOException, TimeoutException {
        this.gui = gui;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, " going to attach server to host: " + host + "...");

        System.out.println(" going to attach server to host: " + host + "... in port " + port + " user: " + user + " pass: " + pass);
        Connection connection = RabbitUtils.newConnection2Server(host, port, user, pass);
        this.channelToRabbitMq = RabbitUtils.createChannel2Server(connection);

        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
        //String[] bindingKeys={"",""};
        this.exchangeBindingKeys=bindingKeys;
        this.messageFormat = messageFormat;

        bindExchangeToChannelRabbitMQ();
        attachConsumerToChannelExchangeWithKey();
    }

    /**
     * Binds the channel to given exchange name and type.
     */
    private void bindExchangeToChannelRabbitMQ() throws IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Declaring Exchange '" + this.exchangeName + "' with type " + this.exchangeType);

        /* TODO: Declare exchange type  */
        //channelToRabbitMq.exchangeDeclare(exchangeName + "server", this.exchangeType);
        channelToRabbitMq.exchangeDeclare(exchangeName, this.exchangeType);
    }

    /**
     * Creates a Consumer associated with an unnamed queue.
     */
    public void attachConsumerToChannelExchangeWithKey() {
        try {
            /* TODO: Create a non-durable, exclusive, autodelete queue with a generated name.
                The string queueName will contain a random queue name (e.g. amq.gen-JzTY20BRgKO-HjmUJj0wLg) */
            String queueName = channelToRabbitMq.queueDeclare().getQueue();

            /* TODO: Create binding: tell exchange to send messages to a queue; fanout exchange ignores the last parameter (binding key) */

            //channelToRabbitMq.queueBind(queueName, exchangeName + "Server", "server.*");
            System.out.println("queue bind: " + queueName  + " exchange: " + exchangeName);
            channelToRabbitMq.confirmSelect();  // chatgpt
            channelToRabbitMq.queueBind(queueName, exchangeName, "server.*");
            //channelToRabbitMq.queuePurge(queueName);
            if (channelToRabbitMq.waitForConfirms()) {  // chatgpt
                System.out.println("Binding operation successful.");
            } else {
                System.out.println("Binding operation failed.");
            }


            Logger.getLogger(this.getClass().getName()).log(Level.INFO, " Created consumerChannel bound to Exchange " + this.exchangeName + "...");

            /* Use a DeliverCallback lambda function instead of DefaultConsumer to receive messages from queue;
               DeliverCallback is an interface which provides a single method:
                void handle(String tag, Delivery delivery) throws IOException; */
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), messageFormat);
                String routingKey = delivery.getEnvelope().getRoutingKey();
                System.out.println("received message " + message + " from routing key " + routingKey);

                //Store the received message
                if(routingKey.equals("server.create")) {
                    // create topic with gameId (message)
                    System.out.println("received message " + message + " from routing key server.create");
                    String newGame[] = message.split(":");
                    waitingPlayers.put(newGame[0], Integer.valueOf(newGame[1])-1);
                    //attachConsumerToChannelExchangeWithKey("server." + message);
                }
                else if(routingKey.equals("server.join")) {
                    // create topic with gameId (message)
                    System.out.println("received message " + message + " from routing key server.create");
                    String newGame[] = message.split(":");
                    waitingPlayers.put(newGame[0], waitingPlayers.get(newGame[0])-1);
                    //attachConsumerToChannelExchangeWithKey("server." + message);
                }
                else {
                    String gameId = routingKey.split("\\.")[1];
                    setReceivedMessage(message, gameId);
                }
                System.out.println(" [x] Consumer Tag [" + consumerTag + "] - Received '" + message + "'");

                // TODO: Notify the GUI about the new message arrive
                //gui.updateTextArea();
            };
            CancelCallback cancelCallback = consumerTag -> {
                System.out.println(" [x] Consumer Tag [" + consumerTag + "] - Cancel Callback invoked!");
            };

            // TODO: Consume with deliver and cancel callbacks
            channelToRabbitMq.basicConsume(queueName, true, deliverCallback, cancelCallback);


        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Publish messages to existing exchange instead of the nameless one.
     * - The routingKey is empty ("") since the fanout exchange ignores it.
     * - Messages will be lost if no queue is bound to the exchange yet.
     * - Basic properties can be: MessageProperties.PERSISTENT_TEXT_PLAIN, etc.
     */
    public void sendMessage(String msgToSend, String routingKey) throws IOException {
        //RoutingKey will be ignored by FANOUT exchange
        System.out.println("sending message: " + msgToSend + " using routing key: " + routingKey);

        // TODO: Publish message
        BasicProperties prop = MessageProperties.PERSISTENT_TEXT_PLAIN;
        channelToRabbitMq.basicPublish(exchangeName, routingKey, prop, msgToSend.getBytes("UTF-8"));
    }

    /**
     * @return the most recent message received from the broker
     */
    public String getReceivedMessage() {
        return receivedMessage;
    }

    /**
     * @param receivedMessage the received message to set
     */
    public void setReceivedMessage(String receivedMessage, String gameId) throws IOException {
        if(waitingPlayers.get(gameId) > 0) {
            // do not send messages until all players join in
            System.out.println("still waiting for " + waitingPlayers.get(gameId));
            return;
        }

        String[] string = receivedMessage.split(":");
        System.out.println("Received message (" + receivedMessage + ") from game " + gameId);
        this.receivedMessage = receivedMessage;

//        if (string[1].equals("Down Pressed") || string[1].equals("Up Pressed") || string[1].equals("Right Pressed") || string[1].equals("Left Pressed")) {
            String routingKey = "client." + gameId;
            this.sendMessage(receivedMessage, routingKey);
//        }

    }
}