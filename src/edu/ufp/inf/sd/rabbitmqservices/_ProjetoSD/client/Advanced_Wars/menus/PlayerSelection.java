package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.menus;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JLabel;

import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game;
import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.menus.MenuHandler;
import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Observer;

/**
 * This deals with player and battle options setup (might split it) such as npc, team, commander, starting money, turn money, fog, etc.
 * @author SergeDavid
 * @version 0.2
 */
public class PlayerSelection implements ActionListener {
	//TODO: Scale with map size.
	//Commander Selection
	JButton[] Prev = {new JButton("Prev"),new JButton("Prev"),new JButton("Prev"),new JButton("Prev")};
	JButton[] Next = {new JButton("Next"),new JButton("Next"),new JButton("Next"),new JButton("Next")};
	JLabel[] Name = {new JLabel("Andy"),new JLabel("Andy"),new JLabel("Andy"),new JLabel("Andy")};
	int[] plyer = {0,0,0,0};
	
	//NPC Stuff
	JButton[] ManOrMachine = {new JButton("PLY"),new JButton("NPC"),new JButton("NPC"),new JButton("NPC")};
	boolean[] empty = {true,true,true,true};
	
	//Other
	JButton Return = new JButton("Return");
	JButton StartMoney = new JButton ("$ 100");int start = 100;
	JButton CityMoney = new JButton ("$ 50");int city = 50;
	JButton ThunderbirdsAreGo = new JButton ("Start");
	
	String mapname;
	GameSessionRI gameSessionRI;
	String game_uid;
	ObserverImpl observer;
	edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.server.Game server_game;
	int player_id;
	
	public PlayerSelection(String map, GameSessionRI gameSessionRI, String game_uid) {
		// creating an observer:
		// - this observer will let us know when the game starts
		// - once the game starts, the observer will communicate with battle

		try {
			if(game_uid == null) {
				// create new game
				String uid = String.valueOf(gameSessionRI.getGames().size());
				Game.uid = uid;
				server_game = gameSessionRI.insertGame(uid, map, empty);
				game_uid = uid;
			}
			else {
				// we will join existing game
				server_game = gameSessionRI.getGame(game_uid);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		this.game_uid = game_uid;

		try {
			observer = new ObserverImpl(server_game.getId(), server_game.getSubjectRI(), this);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		mapname = map;
		this.gameSessionRI = gameSessionRI;
		this.game_uid = game_uid;
		if(game_uid != null)
			ThunderbirdsAreGo.setText("Join");
		Point size = edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.menus.MenuHandler.PrepMenu(400,200);
		for (int i = 0; i < 4; i++) {
			Prev[i].addActionListener(this);
			Prev[i].setBounds(size.x+10+84*i, size.y+10, 64, 32);
			Game.gui.add(Prev[i]);
			Next[i].addActionListener(this);
			Next[i].setBounds(size.x+10+84*i, size.y+100, 64, 32);
			Game.gui.add(Next[i]);
			ManOrMachine[i].addActionListener(this);
			ManOrMachine[i].setBounds(size.x+12+84*i, size.y+68, 58, 24);
			Game.gui.add(ManOrMachine[i]);
			Name[i].setBounds(size.x+10+84*i, size.y+40, 64, 32);
			Game.gui.add(Name[i]);
			// nao queremos que seleccione npc
			if(i >= 1) {
				Name[i].setVisible(false);
				Next[i].setVisible(false);
				Prev[i].setVisible(false);
			}
			ManOrMachine[i].setVisible(false);
		}
		SetBounds(size);
		AddGui();
		AddListeners();
	}
	private void SetBounds(Point size) {
		ThunderbirdsAreGo.setBounds(size.x+200, size.y+170, 100, 24);
		Return.setBounds(size.x+20, size.y+170, 100, 24);
	}
	private void AddGui() {
		Return.addActionListener(this);
		Game.gui.add(ThunderbirdsAreGo);
		Game.gui.add(Return);
	}
	private void AddListeners() {
		ThunderbirdsAreGo.addActionListener(this);
		Return.addActionListener(this);
	}
	
	@Override public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == Return) {
			edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.menus.MenuHandler.CloseMenu();
			Game.gui.LoginScreen();
		}
		else if(s == ThunderbirdsAreGo) {
			// adicionado: adicionar o jogo e criar Subject e Observer
			if(gameSessionRI != null)
				try {
					// join
					Game.uid = game_uid;
					player_id = server_game.getNumPlayers();
					System.out.println("MY player id is going to be " + player_id);
					edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.server.Game g = gameSessionRI.joinGame(game_uid, plyer[0]);
				}
				catch(RemoteException ex) {
					System.out.println("Could not create server game: " + ex);
				}

			// a espera dos outros jogadores...
			MenuHandler.CloseMenu();
		}
		for (int i = 0; i < 4; i++) {
			if (s == Prev[i]) {
				plyer[i]--;
				if (plyer[i]<0) {plyer[i]=Game.displayC.size()-1;}
				Name[i].setText(Game.displayC.get(plyer[i]).name);
			}
			else if (s == Next[i]) {
				plyer[i]++;
				if (plyer[i]>Game.displayC.size()-1) {plyer[i]=0;}
				Name[i].setText(Game.displayC.get(plyer[i]).name);
			}
			else if (s == ManOrMachine[i]) {
				empty[i]=!empty[i];
				if (empty[i]) {ManOrMachine[i].setText("NPC");}
				else {ManOrMachine[i].setText("PLY");}
			}
		}
	}

	public void gameStarts() {
		System.out.println("INFO game starts!");
		Game.btl.NewGame(server_game.getMapa(), server_game.getId(), observer, server_game.getSubjectRI(), player_id);
		Game.btl.AddCommanders(server_game.getPlayers(), server_game.getEmpty(), 100, 50);
		Game.gui.InGameScreen();
	}
}
