package edu.ufp.inf.sd.rmi.ProjetoSD.server;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This class simulates a DBMockup for managing users and books.
 *
 * @author rmoreira
 *
 */
public class DBMockup {

    private final ArrayList<Game> games = new ArrayList<>();
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<SubjectRI> subjectsRI = new ArrayList<>();

    /**
     * This constructor creates and inits the database with some books and users.
     */
    public DBMockup() {
    }

    public ArrayList<Game> getGames() {
        return games;
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    public ArrayList<SubjectRI> getSubjectsRI() {
        return subjectsRI;
    }

    /**
     * Registers a new user.
     *
     * @param u username
     * @param p passwd
     */
    public boolean register(String u, String p) {
        if (!exists(u, p)) {
            users.add(new User(u, p));
            return true;
        }
        return false;
    }

    /**
     * Checks the credentials of an user.
     *
     * @param u username
     * @param p passwd
     * @return
     */
    public boolean exists(String u, String p) {
        for (User usr: users) {
            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
        //return ((u.equalsIgnoreCase("guest") && p.equalsIgnoreCase("ufp")) ? true : false);
    }


    /**
     * Inserts a new game into the DB.
     *
     * @param uid uid
     * @param mapa mapa
     */
    public Game insert(String uid, String mapa, boolean empty[]) throws RemoteException {
        SubjectRI subject = new SubjectImpl(uid);
        Game g = new Game(uid, mapa, empty, subject);
        //g.incNumPlayers();
        games.add(g);
        subjectsRI.add(subject);
        return g;
    }

    public boolean existsGame(String title) {
        for (Game g: games) {
            if (g.getId().compareTo(title) == 0) {
                return true;
            }
        }
        return false;
    }

    public SubjectRI getSubjectById(String n) throws RemoteException {
        for (SubjectRI s: subjectsRI) {
            if(s.getId().compareTo(n) == 0){
                return s;
            }
        }
        System.out.println("Nada");
        return null;
    }
    public Game getGameById(String n) throws RemoteException {
        for (Game g: games) {
            if(g.getId().compareTo(n) == 0){
                return g;
            }
        }
        System.out.println("Nada");
        return null;
    }
}

