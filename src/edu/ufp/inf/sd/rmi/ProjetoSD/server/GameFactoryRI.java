package edu.ufp.inf.sd.rmi.ProjetoSD.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameFactoryRI extends Remote {
    boolean register (String username, String pwd) throws RemoteException;
    GameSessionRI login(String username, String pwd) throws RemoteException;
}
