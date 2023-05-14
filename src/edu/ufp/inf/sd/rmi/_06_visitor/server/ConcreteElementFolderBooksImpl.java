package edu.ufp.inf.sd.rmi._06_visitor.server;

import edu.ufp.inf.sd.rmi._01_helloworld.server.DigLibFactoryRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConcreteElementFolderBooksImpl extends UnicastRemoteObject implements DigLibFactoryRI {


    public ConcreteElementFolderBooksImpl() throws RemoteException {
        super();
    }

    @Override
    public void print(String msg) throws RemoteException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "someone called me with msg = {0}", new Object[]{msg});
    }
}
