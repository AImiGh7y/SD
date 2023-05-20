package edu.ufp.inf.sd.rmi.ProjetoSD.server;

import edu.ufp.inf.sd.rmi.ProjetoSD.client.ObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SubjectImpl extends UnicastRemoteObject implements SubjectRI {

    private String uid;
    private State subjectState;
    private ArrayList<ObserverRI> observers = new ArrayList<>();

    public SubjectImpl(String uid) throws RemoteException {
        super();
        this.uid = uid;
    }

    @Override
    public String getId() {
        return uid;
    }

    @Override
    public void attach(ObserverRI obsRI) {
        observers.add(obsRI);
    }

    @Override
    public void detach(ObserverRI obsRI) {
        observers.remove(obsRI);
    }

    @Override
    public State getState() {
        return subjectState;
    }

    @Override
    public void setState(State state) throws RemoteException{
        // recebe mensagem de um observador
        // state.id (quem enviou), state.msg (mensagem)
        Logger.getLogger(SubjectImpl.class.getName()).log(Level.INFO, "state: {0}", state);
        subjectState = state;
        notifyObservers();
    }

    private void notifyObservers() throws RemoteException {
        for(ObserverRI o: observers) {
            //o.receive(state);
            o.update();
        }
    }

    public void startGame() throws RemoteException {
        for(ObserverRI o: observers)
            o.gameStarts();
    }
}
