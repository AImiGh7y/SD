package edu.ufp.inf.sd.rmi.ProjetoSD.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameSessionImpl extends UnicastRemoteObject implements GameSessionRI {

    GameFactoryImpl gameFactory;
    private String user;

    public GameSessionImpl(GameFactoryImpl gameFactory, String user) throws RemoteException {
        super();
        this.gameFactory = gameFactory;
        this.user = user;
    }

    @Override
    public ArrayList<Game> getGames() throws RemoteException {
        return this.gameFactory.getDb().getGames();
    }

    @Override
    public String getUsername() throws RemoteException {
        return user;
    }

    @Override
    public void logout() throws RemoteException  {
        gameFactory.remover_User(user, this);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Deste logout!");
    }

    public Game insertGame(String title, String mapa, boolean empty[]) throws RemoteException{
        if (!this.gameFactory.getDb().existsGame(title)){
            Game g = this.gameFactory.getDb().insert(title, mapa, empty);
            return g;
        }
        return null;
    }

    public Game joinGame(String t, int player_type) throws RemoteException{
        for (Game g : this.gameFactory.getDb().getGames()){
            if (g.getId().compareTo(t) == 0){
                System.out.println("INFO juntar ao jogo " + t);
                g.incNumPlayers(player_type);
                System.out.println("INFO numero jogadores: " + g.getNumPlayers() + " total: " + g.getMaxPlayers());
                if(g.getNumPlayers() >= g.getMaxPlayers()) {
                    // all players in the game: start
                    ((SubjectImpl)g.getSubjectRI()).startGame();
                }
                return g;
            }
        }
        System.out.println("ERRO jogo nao existe");
        return null;
    }

    public Game quitGame(String t) throws RemoteException{
        for (Game g : this.gameFactory.getDb().getGames()){
            if (g.getId().compareTo(t) == 0){
                g.decNumPlayers();
                return g;
            }
        }
        return null;
    }

    public SubjectRI getSubject(String t) throws RemoteException{
        return this.gameFactory.getDb().getSubjectById(t);
    }

    public Game getGame(String t) throws RemoteException{
        return this.gameFactory.getDb().getGameById(t);
    }
}
