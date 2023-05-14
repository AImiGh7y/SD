package edu.ufp.inf.sd.rmi.ProjetoSD.client.Advanced_Wars.menus;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;

import edu.ufp.inf.sd.rmi.ProjetoSD.client.Advanced_Wars.engine.Game;

//import edu.ufp.inf.sd.rmi.ProjetoSD.server.Game;
import edu.ufp.inf.sd.rmi.ProjetoSD.server.GameSessionRI;

/**
 * This is the opening menu of the game.
 * @author SergeDavid
 * @version 0.2
 */
public class StartMenu implements ActionListener {
	//Single Player
	public JButton New = new JButton("New Game");
	public JButton Load = new JButton("Load Game");

	//Online
	public JButton Join = new JButton("Join Online");
	public JButton Refresh = new JButton("Refresh");
	
	//Other
	public JButton Editor = new JButton("Editor");
	public JButton Credits = new JButton("Credits");
	public JButton Options = new JButton("Options");
	public JButton Exit = new JButton("Exit");
	
	//Map list
	public JList maps_list = new JList();
	DefaultListModel maps_model = new DefaultListModel();
	public JList games_list = new JList();  // adicionado

	private Game game;

	public StartMenu(Game game) {
		this.game = game;
		Point size = MenuHandler.PrepMenu(400,280);
		MenuHandler.HideBackground();
		SetBounds(size);
		AddGui();
		AddListeners();
		MapList(size);
	}

	private void SetBounds(Point size) {
		New.setBounds(size.x,size.y+10, 100, 32);
		Load.setBounds(size.x,size.y+10+38*1, 100, 32);
		Join.setBounds(size.x,size.y+10+38*2, 100, 32);
		Refresh.setBounds(size.x+220-20+140+10, size.y+10-40, 100, 32);
		Editor.setBounds(size.x,size.y+10+38*3, 100, 32);
		Credits.setBounds(size.x,size.y+10+38*4, 100, 32);
		Options.setBounds(size.x,size.y+10+38*5, 100, 32);
		Exit.setBounds(size.x,size.y+10+38*6, 100, 32);
	}
	private void AddGui() {
		Game.gui.add(New);
		Game.gui.add(Load);
		Game.gui.add(Join);
		Game.gui.add(Refresh);
		Game.gui.add(Editor);
		Game.gui.add(Credits);
		Game.gui.add(Options);
		Game.gui.add(Exit);
	}
	private void MapList(Point size) {
		maps_model = Game.finder.GrabMaps();
		JScrollPane maps_pane = new JScrollPane(maps_list = new JList(maps_model));
		maps_pane.setBounds(size.x+220-20, size.y+10, 140, 260);//220,10
		Game.gui.add(maps_pane);
		maps_list.setBounds(0, 0, 140, 260);
		maps_list.setSelectedIndex(0);

		// adicionado lista com os jogos a decorrer
		JScrollPane games_pane = new JScrollPane(games_list);
		games_pane.setBounds(size.x+220-20+140+10, size.y+10, 140, 260);//220,10
		Game.gui.add(games_pane);
		games_list.setBounds(0, 0, 140, 260);
		refreshGamesList();
	}
	private void AddListeners() {
		New.addActionListener(this);
		Load.addActionListener(this);
		Join.addActionListener(this);
		Refresh.addActionListener(this);
		Editor.addActionListener(this);
		Credits.addActionListener(this);
		Options.addActionListener(this);
		Exit.addActionListener(this);
	}

	private void refreshGamesList() {
		GameSessionRI gameSessionRI = game.getGameSessionRI();
		if(gameSessionRI == null)
			return;
		DefaultListModel model = new DefaultListModel();
		try {
			for(edu.ufp.inf.sd.rmi.ProjetoSD.server.Game game: gameSessionRI.getGames()) {
				model.addElement(game.toString());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		games_list.setModel(model);
	}

	@Override public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s==New) {
			String mapa = maps_list.getSelectedValue()+"";
			System.out.println("INFO start menu: go to player selection with map " + mapa);
			new PlayerSelection(mapa, game.getGameSessionRI(), null);
		}
		else if (s==Load) {Game.save.LoadGame();MenuHandler.CloseMenu();}
		else if (s==Join) {
			String mapa = maps_list.getSelectedValue()+"";
			String uid = String.valueOf(games_list.getSelectedIndex());
			Game.uid = uid;

			try {
				edu.ufp.inf.sd.rmi.ProjetoSD.server.Game server_game = game.getGameSessionRI().getGame(uid);
				if(server_game.getNumPlayers() >= server_game.getMaxPlayers())
					// erro: max jogadores
					Game.error.ShowError("No more players. The map only supports " + server_game.getMaxPlayers() + " players.");
				else
					new PlayerSelection(mapa, game.getGameSessionRI(), uid);
			} catch (RemoteException ex) {
				ex.printStackTrace();
			}
		}
		else if(s == Refresh) {
			refreshGamesList();
		}
		else if (s==Editor) {
			Game.edit.StartEditor(
					"MapName",
					16,
					20);
			MenuHandler.CloseMenu();
		}
		else if (s==Credits) {new Credits();}
		else if (s==Options) {new Options(game);}
		else if (s==Exit) {System.exit(0);}
	}
}
