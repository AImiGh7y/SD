package edu.ufp.inf.sd.rmi.ProjetoSD.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;


public class GameFactoryImpl extends UnicastRemoteObject implements GameFactoryRI {

    private DBMockup dbMockup;
    private HashMap<String, GameSessionImpl> sessions;

    public DBMockup getDb() {
        return dbMockup;
    }

    public GameFactoryImpl() throws RemoteException {
        super();
        this.dbMockup = new DBMockup();
        this.sessions = new HashMap<>();
    }

    public DBMockup getDbMockup() {
        return dbMockup;
    }

    @Override
    public boolean register(String username, String pwd) throws RemoteException {
        if(dbMockup.register(username, pwd)) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User registado!");
            return true;
        }
        return false;
    }

    @Override
    public GameSessionRI login(String username, String pwd) throws RemoteException {

        if(dbMockup.exists(username, pwd)) {
            if(!sessions.containsKey(username)){
                GameSessionImpl gameSession = new GameSessionImpl(this, username);
                sessions.put(username, gameSession);
                return gameSession;
            }else{
                return sessions.get(username);
            }
        }
        //throw new RemoteException("Erro! Username ou Palavra Passe Errada!!!!");
        return null;
    }

    public void remover_User(String username, GameSessionImpl gameSession) {
        sessions.remove(username, gameSession);
    }
}
