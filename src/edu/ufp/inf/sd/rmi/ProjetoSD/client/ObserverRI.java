package edu.ufp.inf.sd.rmi.ProjetoSD.client;

import edu.ufp.inf.sd.rmi.ProjetoSD.server.SubjectRI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObserverRI extends Remote {
    public void update() throws RemoteException;
    public String getId() throws RemoteException;
    public SubjectRI getSubjectRI() throws RemoteException;

    public void gameStarts() throws RemoteException;
}
