package edu.ufp.inf.sd.rmi.ProjetoSD.server;

import java.io.Serializable;

public class Game implements Serializable {
    private String uid;
    private String mapa;
    private SubjectRI subjectRI;
    private int[] plyer;
    private boolean[] empty;

    public Game(String uid, String mapa, boolean empty[], SubjectRI subject) {
        System.out.println("INFO create game " + uid + " with map " + mapa);
        this.uid = uid;
        this.mapa = mapa;
        this.subjectRI = subject;
        this.plyer = new int[empty.length];
        this.empty = empty;
    }

    public void printInfo() {
        System.out.println("Game: " + this.getId());
        System.out.println("Mapa: " + this.getMapa());
        System.out.println("NumPlayers: " + this.getNumPlayers());
        System.out.println("------------------------------------------------------------------------------");
    }
    /**
     * @return the title
     */
    public String getMapa() {
        return mapa;
    }

    /**
     * @param mapa dificuldade do jogo
     */
    public void setMapa(String mapa) {
        this.mapa = mapa;
    }

    public int[] getPlayers() {return plyer;}
    public boolean[] getEmpty() {return empty;}

    public int getNumPlayers() {
        int humanPlayers = 0;
        for(boolean b: empty)
            if(!b)
                humanPlayers += 1;
        return humanPlayers;
    }

    public int getMaxPlayers() {
        //return empty.length;
        if(mapa.compareTo("SmallVs") == 0)
            return 2;
        return 4;
    }

    public void incNumPlayers(int player_type) {
        for(int i = 0; i < empty.length; i++)
            if(empty[i]) {
                empty[i] = false;
                plyer[i] = player_type;
                break;
            }
    }

    public void decNumPlayers() {
        for(int i = 0; i < empty.length; i++)
            if(!empty[i]) {
                empty[i] = true;
                break;
            }
    }

    public SubjectRI getSubjectRI() { return subjectRI; }
    public String getId() { return uid; }

    public String toString() {
        return "id: " + uid + " mapa: " + mapa + " nplayers: " + getNumPlayers();
    }
}
