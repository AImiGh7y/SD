package edu.ufp.inf.sd.rmi.ProjetoSD.client;

import edu.ufp.inf.sd.rmi.ProjetoSD.client.Advanced_Wars.engine.Battle;
import edu.ufp.inf.sd.rmi.ProjetoSD.client.Advanced_Wars.menus.PlayerSelection;
import edu.ufp.inf.sd.rmi.ProjetoSD.server.State;
import edu.ufp.inf.sd.rmi.ProjetoSD.server.SubjectRI;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObserverImpl extends UnicastRemoteObject implements ObserverRI {

    private String id;
    private State lastObserverState;
    protected SubjectRI subjectRI;
    private PlayerSelection menu;
    private Battle battle;

    public ObserverImpl(String id, SubjectRI subjectRI, PlayerSelection menu) throws RemoteException {
        super();
        this.id = id;
        this.subjectRI = subjectRI;
        this.subjectRI.attach(this);
        this.menu = menu;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public void update(edu.ufp.inf.sd.rmi.ProjetoSD.server.Game g) throws RemoteException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "update");
        lastObserverState = subjectRI.getState();
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "lastObserverState: {0}", lastObserverState);
    }

    public State getLastObserverState() {
        return lastObserverState;
    }

    public String getId() {
        return id;
    }

    public SubjectRI getSubjectRI() {
        return subjectRI;
    }

    public void gameStarts() {
        menu.gameStarts();
    }

    public void Action() {battle.Action2();}

    public void CaptureCapital(int x, int y) {battle.CaptureCapital2(x, y);}

    public void Buyunit(int type, int x, int y) {
        battle.Buyunit2(type, x, y);
    }

    public void EndTurn() {
        battle.EndTurn2();
    }
}
