package edu.ufp.inf.sd.rmi._04_diglib.server;

import edu.ufp.inf.sd.rmi._04_diglib.server.DigLibSessionRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DigLibSessionImpl extends UnicastRemoteObject implements DigLibSessionRI {

    DigLibFactoryRI digLibFactoryRI;
    private String user;

    public DigLibSessionImpl(DigLibFactoryRI digLibFactoryRI, String user) throws RemoteException {
        super();
        this.digLibFactoryRI = digLibFactoryRI;
        this.user = user;
    }

    @Override
    public Book[] search(String title, String author) throws RemoteException  {
        return ((DigLibFactoryImpl)digLibFactoryRI).getDbMockup().select(title, author);
    }

    @Override
    public void logout() throws RemoteException  {
        ((DigLibFactoryImpl)digLibFactoryRI).remover_User(user, this);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Deste logout!");
    }
}
