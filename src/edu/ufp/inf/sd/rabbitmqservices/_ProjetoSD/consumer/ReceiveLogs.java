package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.consumer;

/**
 * 1. Run the rabbitmq server as a shell process, by calling:
 * $ rabbitmq-server
 *
 * <p>
 * 2. Run consumer Recv that keeps running, waiting for messages from publisher Send (Use Ctrl-C to stop):
 * $ ./runconsumer
 *
 * <p>
 * 3. Run publisher Send several times from terminal (will send mesg "hello world"):
 * $ ./runproducer
 *
 * <p>
 * 4. Check RabbitMQ Broker runtime info (credentials: guest/guest4rabbitmq) from:
 * http://localhost:15672/
 *
 * @author rui
 */

import com.rabbitmq.client.*;
import edu.ufp.inf.sd.rabbitmqservices.util.RabbitUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveLogs {

    /**
     * Run consumer Recv that keeps running, waiting for messages from publisher Send (Use Ctrl-C to stop):
     * $ ./runconsumer
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            RabbitUtils.printArgs(args);

            //Read args passed via shell command
            String host=args[0];
            int port=Integer.parseInt(args[1]);
            //String queueName=args[2];
            String exchangeName=args[2];


            // Open a connection and a channel to rabbitmq broker/server
            Connection connection=RabbitUtils.newConnection2Server(host, port, "guest", "guest");
            Channel channel=RabbitUtils.createChannel2Server(connection);

            //Declare queue from which to consume (declare it also here, because consumer may start before publisher)
            //channel.queueDeclare(queueName, false, false, false, null);
            //channel.queueDeclare(Send.QUEUE_NAME, true, false, false, null);
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);

            String queueName = channel.queueDeclare().getQueue();

            String routingKey = "";
            channel.queueBind(queueName, exchangeName, routingKey);

            Logger.getAnonymousLogger().log(Level.INFO, Thread.currentThread().getName()+": Will create Deliver Callback...");
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("[X] Consumer Tag [" + consumerTag + "] - Received '" + message + "'");
            };
            CancelCallback cancelCallback = (consumerTag) -> {
                System.out.println("[X] Consumer Tag [" + consumerTag + "] - Cancel Callback invoked!");
            };
            channel.basicConsume(queueName, true, deliverCallback, cancelCallback);

        } catch (Exception e) {
            //Logger.getLogger(Recv.class.getName()).log(Level.INFO, e.toString());
            e.printStackTrace();
        }
    }
}