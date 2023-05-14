package edu.ufp.inf.sd.rmi._03_pingpong.server;

import edu.ufp.inf.sd.rmi._03_pingpong.client.PongRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PingImpl extends UnicastRemoteObject implements PingRI {

    public PingImpl() throws RemoteException {
        super();
    }

    @Override
    public void ping(Ball ball, PongRI pongRI) throws RemoteException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Ping foi chamado");

        //pongRI.pong(ball);
        PingRunnable p2 = new PingRunnable(ball,pongRI);
        new Thread(p2).start();
    }
}
