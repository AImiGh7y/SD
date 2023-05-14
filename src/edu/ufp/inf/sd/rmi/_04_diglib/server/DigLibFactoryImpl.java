package edu.ufp.inf.sd.rmi._04_diglib.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;


public class DigLibFactoryImpl extends UnicastRemoteObject implements DigLibFactoryRI {

    private DBMockup dbMockup;
    private HashMap<String, DigLibSessionImpl> sessions;

    public DigLibFactoryImpl() throws RemoteException {
        super();
         this.dbMockup = new DBMockup();
         this.sessions = new HashMap<>();
    }

    public DBMockup getDbMockup() {
        return dbMockup;
    }

    @Override
    public boolean register(String username, String pwd) throws RemoteException {
        dbMockup.register(username, pwd);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "User registado!");
        return false;
    }

    @Override
    public DigLibSessionRI login(String username, String pwd) throws RemoteException {

        if(dbMockup.exists(username, pwd)) {
            if(!sessions.containsKey(username)){
                DigLibSessionImpl digLibSession = new DigLibSessionImpl(this, username);
                sessions.put(username, digLibSession);
                return digLibSession;
            }else{
                return sessions.get(username);
            }
        }
        //throw new RemoteException("Erro! Username ou Palavra Passe Errada!!!!");
        return null;
    }

    public void remover_User(String username, DigLibSessionImpl digLibSession) {
        sessions.remove(username, digLibSession);
    }
}
