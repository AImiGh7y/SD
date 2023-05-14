package edu.ufp.inf.sd.rmi.ProjetoSD.client;

import edu.ufp.inf.sd.rmi.ProjetoSD.server.GameFactoryRI;
import edu.ufp.inf.sd.rmi.ProjetoSD.server.GameSessionRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import javax.swing.*;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Title: Projecto SD</p>
 * <p>
 * Description: Projecto apoio aulas SD</p>
 * <p>
 * Copyright: Copyright (c) 2017</p>
 * <p>
 * Company: UFP </p>
 *
 * @author Rui S. Moreira
 * @version 3.0
 */
public class GameClient {

    /**
     * Context for connecting a RMI client MAIL_TO_ADDR a RMI Servant
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */
    private GameFactoryRI gameFactoryRI;

    public static void main(String[] args) {
        if (args != null && args.length < 2) {
            System.err.println("usage: java [options] edu.ufp.sd.inf.rmi.ProjetoSD.client.GameClient <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            //1. ============ Setup client RMI context ============
            GameClient hwc = new GameClient(args);
            //2. ============ Lookup service ============
            hwc.lookupService();
            //3. ============ Registo / Login ============
            // JavaFX ou Swing ?
            //4. ============ Play with service ============
            hwc.playService();
        }
    }

    public GameClient(String[] args) {
        try {
            //List ans set args
            SetupContextRMI.printArgs(this.getClass().getName(), args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Remote lookupService() {
        try {
            //Get proxy MAIL_TO_ADDR rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going MAIL_TO_ADDR lookup service @ {0}", serviceUrl);
                
                //============ Get proxy MAIL_TO_ADDR HelloWorld service ============
                gameFactoryRI = (GameFactoryRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return gameFactoryRI;
    }
    
    private void playService() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] fields = {"Username:", usernameField, "Password:", passwordField};
        int option = JOptionPane.showOptionDialog(null, fields, "Login", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Login", "Register"}, null);

        if (option == JOptionPane.YES_OPTION) {  // LOGIN
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            System.out.println("Login\nUsername: " + username + "\nPassword: " + password);

            GameSessionRI gameSessionRI = null;
            try {
                gameSessionRI = gameFactoryRI.login(username, password);
                if (gameSessionRI == null){
                    JOptionPane.showMessageDialog(null, "Invalid user or password",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {  // bem sucedido
                    //menuGame(gameSessionRI);
                    new edu.ufp.inf.sd.rmi.ProjetoSD.client.Advanced_Wars.engine.Game(gameSessionRI);
                }

            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        } else if (option == JOptionPane.NO_OPTION) {  // REGISTER
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            System.out.println("Register\nUsername: " + username + "\nPassword: " + password);
            try {
                if(gameFactoryRI.register(username, password)) {
                    JOptionPane.showMessageDialog(null, "User registered. Game will start.",
                            "Registered", JOptionPane.INFORMATION_MESSAGE);
                    GameSessionRI gameSessionRI = gameFactoryRI.login(username, password);
                    new edu.ufp.inf.sd.rmi.ProjetoSD.client.Advanced_Wars.engine.Game(gameSessionRI);
                }
                else
                    JOptionPane.showMessageDialog(null, "User already registered.",
                            "Error", JOptionPane.ERROR_MESSAGE);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going MAIL_TO_ADDR finish, bye. ;)");
        //} catch (RemoteException ex) {
        //    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        //}
    }
}
