package edu.ufp.inf.sd.rmi._03_pingpong.client;

import edu.ufp.inf.sd.rmi._03_pingpong.server.Ball;
import edu.ufp.inf.sd.rmi._03_pingpong.server.PingRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PongImpl extends UnicastRemoteObject implements PongRI {

    public PongImpl(PingRI pingRI) throws RemoteException {
        super();
        Ball ball = new Ball(1);
        pingRI.ping(ball, this);
    }

    @Override
    public void pong(Ball ball) throws RemoteException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Pong foi chamado");
        //pingRI.ping(ball, this);

    }
}
