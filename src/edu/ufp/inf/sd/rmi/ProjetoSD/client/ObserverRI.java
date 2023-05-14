package edu.ufp.inf.sd.rmi.ProjetoSD.client;

import edu.ufp.inf.sd.rmi.ProjetoSD.server.SubjectRI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObserverRI extends Remote {
    public void update(edu.ufp.inf.sd.rmi.ProjetoSD.server.Game g) throws RemoteException;
    public String getId() throws RemoteException;
    public SubjectRI getSubjectRI() throws RemoteException;

    public void gameStarts() throws RemoteException;

    public void Action() throws RemoteException;
    public void CaptureCapital(int x, int y) throws RemoteException;
    public void Buyunit(int type, int x, int y) throws RemoteException;
    public void EndTurn() throws RemoteException;
}
