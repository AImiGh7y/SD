/**
 * <p>
 * Title: Projecto SD</p>
 * <p>
 * Description: Projecto apoio aulas SD</p>
 * <p>
 * Copyright: Copyright (c) 2011</p>
 * <p>
 * Company: UFP </p>
 *
 * @author Rui Moreira
 * @version 2.0
 */
package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client;

import com.rabbitmq.client.BuiltinExchangeType;
import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game;
import edu.ufp.inf.sd.rabbitmqservices.util.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rjm
 */
public class ObserverClient {

    private edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Observer observer;
    private String generalTopic;

    /**
     * Creates new form ChatClientFrame
     *
     * @param args
     */
    public ObserverClient(String args[]) {
        try {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, " After initComponents()...");

            //initObserver(args);
            RabbitUtils.printArgs(args);

            //Read args passed via shell command
            String host=args[0];
            int port=Integer.parseInt(args[1]);
            String exchangeName=args[2];
            int player = Integer.valueOf(args[3]);
            String mapa = args[4];

            //2. Create the _05_observer object that manages send/receive of messages to/from rabbitmq
            //this.observer=new Observer(this, host, port, "guest", "guest", room, user, this.generalTopic, exchangeName, BuiltinExchangeType.TOPIC, "UTF-8");

            Thread thread = new Thread(){
                public void run(){
                    Game f = null;
                    f = new Game(player);
                    //2. Create the _05_observer object that manages send/receive of messages to/from rabbitmq
                    try {
                        observer = new Observer(f, host, port, "guest", "guest", player, mapa, exchangeName, BuiltinExchangeType.FANOUT, "UTF-8");
                    } catch (IOException | TimeoutException e) {
                        e.printStackTrace();
                    }
                    Logger.getLogger(this.getClass().getName()).log(Level.INFO, " After initObserver()...");
                    f.setObserver(observer);

                    f.GameLoop();
                }
            };
            thread.start();

        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, " After initObserver()...");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                int expectedArgs = 4;
                if (args.length >= expectedArgs) {
                    new ObserverClient(args);
                } else {
                    Logger.getLogger(ObserverClient.class.getName()).log(Level.INFO, "check args.length < "+expectedArgs+"!!!" );
                }
            }
        });
    }}
