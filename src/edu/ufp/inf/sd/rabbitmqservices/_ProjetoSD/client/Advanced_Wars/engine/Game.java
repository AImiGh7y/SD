package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine;

import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.*;
import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Gui;
import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Map;
import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units.Base;
import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Observer;

import java.awt.Dimension;
import java.awt.Image;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

public class Game extends JFrame {
	private static final long serialVersionUID = 1L;
	
	//Application Settings
	private static final String build = "0";
	private static final String version = "2";
	public static final String name = "Strategy Game";
	public static int ScreenBase = 32;//Bit size for the screen, 16 / 32 / 64 / 128
	public static boolean dev = true;//Is this a dev copy or not... useless? D:
	
	public enum State {STARTUP, MENU, PLAYING, EDITOR};
	public static State GameState = State.STARTUP;
		
	//Setup the quick access to all of the other class files.
	public static edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Map map;
	public static edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Gui gui;
	public static LoadImages load;
	public static InputHandler input;
	public static Editor edit = new Editor();
	public static Battle btl = new Battle();
	public static ErrorHandler error = new ErrorHandler();
	public static Pathfinding pathing = new Pathfinding();
	public static ListData list;
	public static Save save = new Save();
	public static ComputerBrain brain = new ComputerBrain();
	public static FileFinder finder = new FileFinder();
	public static ViewPoint view = new ViewPoint();
	
	//Image handling settings are as follows
	public int fps;
	public int fpscount;
	public static Image[] img_menu = new Image[5];
	public static Image img_tile;
	public static Image img_char;
	public static Image img_plys;
	public static Image img_city;
	public static Image img_exts;
	public static Boolean readytopaint;
	
	//This handles the different edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.players and also is used to speed logic arrays (contains a list of all characters they own)
	public static List<edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.players.Base> player = new ArrayList<edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.players.Base>();
	public static List<edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings.Base> builds = new ArrayList<edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings.Base>();
	public static List<Base> units = new ArrayList<Base>();
	//These are the lists that will hold commander, building, and unit data to use in the menu's
	public static List<edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.players.Base> displayC = new ArrayList<edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.players.Base>();
	public static List<edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings.Base> displayB = new ArrayList<edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings.Base>();
	public static List<Base> displayU = new ArrayList<Base>();

	public static String uid;
	private Observer observer;
	public static Game game;
	private int playerId;

	public void setObserver(Observer observer) {
		this.observer = observer;
	}

	public Observer getObserver() {
		return observer;
	}

	public int getPlayerId() {
		return playerId;
	}

	public Game(String mapa, int playerId) {
		super(name);
		System.out.println("Game()");
		this.game = this;
		this.playerId = playerId;
		//Default Settings of the JFrame
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(new Dimension(20*ScreenBase+6,12*ScreenBase+12));
		setBounds(0,0,20*ScreenBase+6,12*ScreenBase+12);
	    setUndecorated(false);
		setResizable(false);
	    setLocationRelativeTo(null);
				
		//Creates all the edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.gui elements and sets them up
		gui = new Gui(this);
		add(gui);
		gui.setFocusable(true);
		gui.requestFocusInWindow();
		
		//load images, initialize the map, and adds the input settings.
		load = new LoadImages();
		map = new Map();
		input = new InputHandler(this);
		list = new ListData();
		
		setVisible(true);//This has been moved down here so that when everything is done, it is shown.
		//gui.LoginScreen();

		String gameId = "";
		int[] players = {0,0,0,0};
		boolean[] empty = {false,false,false,false};
		Game.btl.NewGame(mapa, gameId, observer, playerId);
		Game.btl.AddCommanders(players, empty, 100, 50);
		Game.gui.InGameScreen();

		gui.InGameScreen();
		save.LoadSettings();
		//GameLoop();
	}

	public void GameLoop() {
		boolean loop=true;
		long last = System.nanoTime();
		long lastCPSTime = 0;
		long lastCPSTime2 = 0;
		@SuppressWarnings("unused")
		int logics = 0;
		logics++;
		while (loop) {
			//Used for logic stuff
			@SuppressWarnings("unused")
			long delta = (System.nanoTime() - last) / 1000000;
			delta++;
			last = System.nanoTime();
			
			//FPS settings
			if (System.currentTimeMillis() - lastCPSTime > 1000) {
				lastCPSTime = System.currentTimeMillis();
				fpscount = fps;
				fps = 0;
				error.ErrorTicker();
				setTitle(name + " v" + build + "." + version + " : FPS " + fpscount);
				// npc desativado
/*				if (GameState == State.PLAYING) {
					if (player.get(btl.currentplayer).npc&&!btl.GameOver) {
						brain.ThinkDamnYou(player.get(btl.currentplayer));
					}
				}*/
			}
			else fps++;
			//Current Logic and frames per second location (capped at 20 I guess?)
			if (System.currentTimeMillis() - lastCPSTime2 > 100) {
				lastCPSTime2 = System.currentTimeMillis();
				logics = 0;
				if (GameState==State.PLAYING || GameState==State.EDITOR) {
					view.MoveView();
				}//This controls the view-point on the map
				if (GameState == State.EDITOR) {
					if (edit.holding && edit.moved) {edit.AssButton();}
				}
				Game.gui.frame++;//This is controlling the current frame of animation.
				if (Game.gui.frame>=12) {Game.gui.frame=0;}
				gui.repaint();
			}
			else logics++;
			
			//Paints the scene then sleeps for a bit.
			try { Thread.sleep(30);} catch (Exception e) {};
		}
	}

	public static void updateGame(String play) {
		System.out.println("play: " + play);
		String[] play_args = play.split(":");
		int p = Integer.valueOf(play_args[0]);
		play = play_args[1];
		edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.players.Base ply = Game.player.get(p);
		if(play.startsWith("buy")) {  // "buy type x y"
			String[] args = play.split(" ");
			btl.Buyunit2(Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
		}
		switch(play) {
			case "up":
				ply.selecty--;
				if (ply.selecty<0) {
					ply.selecty++;
				}
				break;

			case "down":
				ply.selecty++;
				if (ply.selecty>=Game.map.height) {
					ply.selecty--;
				}
				break;

			case "left":
				ply.selectx--;
				if (ply.selectx<0) {
					ply.selectx++;
				}
			break;

			case "right":
				ply.selectx++;
				if (ply.selectx>=Game.map.width) {
					ply.selectx--;
				}
				break;

			case "select":
				Game.btl.Action();
				break;

			case "cancel":
				Game.player.get(Game.btl.currentplayer).Cancle();
				break;

			case "start":
				new edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.menus.Pause();
				break;

			case "endturn":
				btl.EndTurn2();
				break;
		}
	}
	
	/**Starts a new game when launched.*/
	/*public static void main(String args[]) throws Exception {
		String currentWorkingDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
		System.out.println(currentWorkingDirectory);

		new Game(0);
	}*/
}
