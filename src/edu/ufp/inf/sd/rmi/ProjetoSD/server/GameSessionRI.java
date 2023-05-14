package edu.ufp.inf.sd.rmi.ProjetoSD.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GameSessionRI extends Remote {
    void logout() throws RemoteException ;
    public String getUsername() throws RemoteException;
    public ArrayList<Game> getGames() throws RemoteException;
    public Game insertGame(String title, String mapa, boolean empty[]) throws RemoteException;
    public Game quitGame(String t) throws RemoteException;
    public SubjectRI getSubject(String t) throws RemoteException;
    public Game getGame(String t) throws RemoteException;
    Game joinGame(String t, int player_type) throws RemoteException;
}

