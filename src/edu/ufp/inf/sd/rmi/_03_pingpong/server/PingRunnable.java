package edu.ufp.inf.sd.rmi._03_pingpong.server;

import edu.ufp.inf.sd.rmi._03_pingpong.client.PongImpl;
import edu.ufp.inf.sd.rmi._03_pingpong.client.PongRI;

import java.rmi.RemoteException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PingRunnable implements Runnable{

    private final PongRI pongRI;
    private final Ball ball;

    public PingRunnable(Ball ball, PongRI pongRI) {
        this.ball=ball;
        this.pongRI=pongRI;
    }

    public void run() {
        try {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Thread foi chamado");

            pongRI.pong(ball);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
