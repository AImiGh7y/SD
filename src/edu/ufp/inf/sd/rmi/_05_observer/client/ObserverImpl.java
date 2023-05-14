package edu.ufp.inf.sd.rmi._05_observer.client;

import edu.ufp.inf.sd.rmi._05_observer.server.State;
import edu.ufp.inf.sd.rmi._05_observer.server.SubjectRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObserverImpl extends UnicastRemoteObject implements ObserverRI {

    private String id;
    private State lastObserverState;
    protected SubjectRI subjectRI;
    protected ObserverGuiClient chatFrame;

    public ObserverImpl(String id, ObserverGuiClient f, SubjectRI subjectRI) throws RemoteException {
        super();
        this.id = id;
        this.chatFrame = f;
        this.subjectRI = subjectRI;
        this.subjectRI.attach(this);
    }

    public void update() throws RemoteException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "update");
        lastObserverState = subjectRI.getState();
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "lastObserverState: {0}", lastObserverState);
        chatFrame.updateTextArea();
    }

    public State getLastObserverState() {
        return lastObserverState;
    }

}
