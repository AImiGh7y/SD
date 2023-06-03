package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.*;
import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game;
import edu.ufp.inf.sd.rabbitmqservices.util.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create a chat room (like zoom chat private msg), supporting:
 * - open *general* messages to all users
 * - private messages to a specific *user*
 *
 * <p>
 * Each _05_observer will receive messages from its queue with 2 Binding keys:
 * - room1.general (public msg for general/all users) and room1.pedro (private msg for given user)
 *
 * <p>
 * Send message with specific Routing keys:
 * - routingKey="room1.general" (public to general/all users)
 * - routingKey="room1.pedro"   (private to specific user)
 *
 * <p>
 * Run _05_observer with 3 parameters <room> <user> <general>:
 * $ runobserver room1 pedro general
 *
 *
 * @author rui
 */
public class Observer {
    //Preferences for exchange...
    private final Channel channelToRabbitMq;
    private final String exchangeName;
    private final BuiltinExchangeType exchangeType;
    //private final String exchangeBindingKeys;
    private final String messageFormat;

    //Settings for specifying topics
    private final Game game;
    private final String mapa;
    private final int player;

    //Store received message to be get by gui
    private String receivedMessage;
    private String gameId;

    public Observer(Game game, String host, int port, String brokerUser, String brokerPass, int player, String mapa, String exchangeName, BuiltinExchangeType exchangeType, String messageFormat, String joinOrCreate, String gameId) throws IOException, TimeoutException {
        this.game = game;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, " going to attach observer to host: " + host + "...");

        Connection connection=RabbitUtils.newConnection2Server(host, port, brokerUser, brokerPass);
        this.channelToRabbitMq=RabbitUtils.createChannel2Server(connection);
        this.exchangeName=exchangeName;
        this.exchangeType=exchangeType;
        this.gameId = gameId;
        //this.exchangeBindingKeys = exchangeBindingKeys;

        this.player=player;
        this.mapa = mapa;

        this.messageFormat=messageFormat;

        bindExchangeToChannelRabbitMQ();
        attachConsumerToChannelExchangeWithKey();

        // ask server to create a new game
        int nplayers = 4;
        if(mapa.equals("SmallVs"))
            nplayers = 2;
        String routingKey = "server." + joinOrCreate;
        System.out.println("telling server to create new game " + gameId);
        String message = gameId + ":" + nplayers;
        this.channelToRabbitMq.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
    }

    /**
     * Declare exchange of specified type.
     */
    private void bindExchangeToChannelRabbitMQ() throws IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Declaring Exchange '" + this.exchangeName + "' with policy = " + this.exchangeType);

        // TODO: Declare exchange type
        this.channelToRabbitMq.exchangeDeclare(exchangeName, exchangeType);
    }

    /**
     * Creates a Consumer associated with an unnamed queue.
     */
    private void attachConsumerToChannelExchangeWithKey() {
        try {
            // TODO: Create a non-durable, exclusive, autodelete queue with a generated name.
            String queueName = this.channelToRabbitMq.queueDeclare().getQueue();

            // TODO: Bind to each routing key (received from args[3] upward)
            String routingKey = "client." + gameId;
            System.out.println("queue bind: " + queueName  + " exchange: " + exchangeName);
            this.channelToRabbitMq.queueBind(queueName, exchangeName, routingKey);

            Logger.getLogger(this.getClass().getName()).log(Level.INFO, " Created consumerChannel bound to Exchange " + this.exchangeName + "...");


            /* Use a DeliverCallback lambda function instead of DefaultConsumer to receive messages from queue;
               DeliverCallback is an interface which provides a single method:
                void handle(String tag, Delivery delivery) throws IOException; */
            DeliverCallback deliverCallback=(consumerTag, delivery) -> {
                String message=new String(delivery.getBody(), "UTF-8");
                System.out.println("received message " + message + " from routing key " + delivery.getEnvelope().getRoutingKey());

                setReceivedMessage(message);
                System.out.println(" [x] Received '" + message + "'");
                game.updateGame(message);
            };
            CancelCallback cancelCallback=consumerTag -> {
                System.out.println(" [x] Consumer Tag [" + consumerTag + "] - Cancel Callback invoked!");
            };

            // TODO: Consume with deliver and cancel callbacks
            this.channelToRabbitMq.basicConsume(queueName, true, deliverCallback, cancelCallback);

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
    public void sendMessage(String msgToSend) throws IOException {
        //RoutingKey will be ignored by FANOUT exchange
        String routingKey = "server." + gameId;

        //System.out.println(msgToSend);

        // TODO: Publish message
        //BasicProperties prop = MessageProperties.PERSISTENT_TEXT_PLAIN;
        System.out.println("sending message (" + msgToSend + ") to routing key " + routingKey);
        this.channelToRabbitMq.basicPublish(exchangeName, routingKey, null, msgToSend.getBytes("UTF-8"));
    }

    /**
     * @return the receivedMessage
     */
    public String getReceivedMessage() {
        return receivedMessage;
    }

    /**
     * @param receivedMessage the receivedMessage to set
     */
    public void setReceivedMessage(String receivedMessage) {
        System.out.println("received message: " + receivedMessage);
        this.receivedMessage=receivedMessage;
    }
}
