package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine;

import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Properties;

/**
 * When SaveGame() is called upon it grabs a screen shot of the current battle (current_player,days) and the important variables from the player, edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings, and unit lists.
 * It currently saves them in a property file, with each saved variable having their own line (it gets very long fast).
 * When LoadGame() is called, it will create a new battle, change the current day and player, and then loads the edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.players / edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units / cities from here.
 * An example is Unit#_Health = 50;
 * @author SergeDavid
 * @version 0.2
 */
public class Save {
	final String path = "saves/";
	
	public void SaveSettings() {
		//If the folder doesn't exist, create it so we can save our save files inside it.
		File directory = new File(path);
		if (!directory.exists()) {
			if (directory.mkdir()) {
                edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.error.ShowError("The " + path + " directory has been created.");}
	    	else {
                edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.error.ShowError("Cannot create the save directory. " + path + "");return;}
		}
		//Saves the game in a property file (since it is easy)
		try {
			FileWriter fstream = new FileWriter(path + "PlayerData.properties");
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write(
		    		"# Current testing setup for saving player data. To be improved when I finally get around to adding options\n" +
		    		"# If you enable developer keys then they will override the number pad (return to default key)\n" +
		    		"Player = Player\n" +
		    		"ScreenSize = " + edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.ScreenBase + "\n" +
		    		"TexturePack = " + "UnAddedFeature" + "\n" +
		    		"Dev = " + edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.dev + "\n" +
		    		"Volume = 0\n" +
		    		"Music = 0");
		    out.close();
		} 
		catch (Exception e) {
            edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.error.ShowError("Saving PlayerData failed:" + e.getMessage());}
	}
	public void LoadSettings() {
		try {
			//Opens the property file and starts a battle with the map in the save folder. 
			Properties configFile = new Properties();
			configFile.load(new FileInputStream("C:\\Users\\Nuno\\SD\\saves\\PlayerData.properties"));

			edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.dev = ( Boolean.parseBoolean(configFile.getProperty("Dev", "false")));
			edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.gui.ResizeScreen( Integer.parseInt(configFile.getProperty("ScreenSize", "32")));
		} 
		catch (Exception e) {
            edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.error.ShowError("Player data failed to load."); e.printStackTrace();}
	}
	
	public void SaveGame() {
		//If the folder doesn't exist, create it so we can save our save files inside it.
		File directory = new File(path);
		if (!directory.exists()) {
			if (directory.mkdir()) {
                edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.error.ShowError("The " + path + " directory has been created.");}
	    	else {
                edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.error.ShowError("Cannot create the save directory. " + path + "");return;}
		}
		//Saves the game in a property file (since it is easy)
		try {
			FileWriter fstream = new FileWriter(path + "savegame.properties");
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write("# A testing save file format for my advanced wars project, using property file as it is a list of properties...\n" +
		    		  "# When loading map, skip the unit stage (will load edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units from here, maybe edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings too with health)" +
		    		  "# I might change this to something close to the map.txt files\n" +
		    		  "Map = " + edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.mapname + "\n" +
		    		  "CurrentPlayer = " + edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.currentplayer + "\n" +
		    		  "Days = " + edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.day + "\n" +
		    		  "Units = " + edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.units.size() + "\n");
		    out.write(SavePlayerData());
		    out.write(SaveCityData());
		    out.write(SaveUnitData());
		    out.close();
		} 
		catch (Exception e) {
            edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.error.ShowError("Save Failed:" + e.getMessage());}
	}
	private String SavePlayerData() {
		String plyrdata = "\n# Player Data\n";
		for (int i = 0; i < edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.totalplayers; i++) {
	    	edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.players.Base ply = edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.player.get(i);
	    	plyrdata+="Player" + i + "_Type = " + ply.name + "\n" +//This is the character (commander) that said player is using. (Using name to find them)
	    			  "Player" + i + "_Defeated = " + false + "\n" +//If true, this character has lost. (Out of the game)
	    			  "Player" + i + "_Team = " + ply.team + "\n" +//Current monetary balance
	    			  "Player" + i + "_Money = " + ply.money + "\n" +//Current monetary balance
	    			  "Player" + i + "_Kills = " + ply.kills + "\n" +//How many edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units they've killed
	    			  "Player" + i + "_Loses = " + ply.loses + "\n" +//How many edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units they've lost
	    			  "Player" + i + "_Power = " + ply.power + "\n" +//Current power level
	    			  "Player" + i + "_Using = " + 0 + "\n" +//0 = none, 1 = using first power, 2 = using 2nd power
	    			  "Player" + i + "_Npc = " + ply.npc + "\n";
		}
		return plyrdata;
	}
	private String SaveCityData() {
		String citydata = "\n# City Data\n";
		for (int i = 0; i < edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.builds.size(); i++) {
	    	edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings.Base bld = edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.builds.get(i);
	    	citydata+="City" + i + "_Type = " + bld.name + "\n" +
	    			  "City" + i + "_Owner = " + bld.owner + "\n" +
	    			  "City" + i + "_Health = " + bld.health + "\n";
	    }
		return citydata;
	}
	private String SaveUnitData() {
		String unitdata = "\n# Unit Data\n";
	    for (int i = 0; i < edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.units.size(); i++) {
	    	edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units.Base unit = edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.units.get(i);
	    	unitdata+="Unit" + i + "_Type = " + unit.name + "\n" +
	    			  "Unit" + i + "_Owner = " + unit.owner + "\n" +
	    			  "Unit" + i + "_Health = " + unit.health + "\n" +
	    			  "Unit" + i + "_Ammo = " + unit.Ammo + "\n" +
	    			  "Unit" + i + "_Fuel = " + unit.Fuel + "\n" +
	    			  "Unit" + i + "_X = " + unit.x + "\n" +
	    			  "Unit" + i + "_Y = " + unit.y + "\n" +
	    			  "Unit" + i + "_Acted = " + !unit.acted + "\n";  
	    }
		return unitdata;
	}

	public void LoadGame() {
		try {
			//Opens the property file and starts a battle with the map in the save folder. 
			Properties configFile = new Properties();
			configFile.load(new FileInputStream(System.getProperty("user.dir") + "/" + path + "savegame.properties"));
			edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.NewGame(configFile.getProperty("Map"), null, null, null, 0);
			edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.currentplayer = Integer.parseInt(configFile.getProperty("CurrentPlayer"));
			edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.day = Integer.parseInt(configFile.getProperty("Days"));

			edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.units = new ArrayList<edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units.Base>();
			
			//Player Setup 
			LoadPlayers(configFile);
			LoadCities(configFile);
			LoadUnits(configFile);
			
			//Changes the edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.gui to match the appropriate bla bla bla
			edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.gui.InGameScreen();
		} 
		catch (Exception e) {
            edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.error.ShowError("Saved game failed to load."); e.printStackTrace();}
	}
	private void LoadPlayers(Properties configFile) {
		for (int i = 0; i < edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.btl.totalplayers; i++) {
			edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.list.LoadCommander(
					configFile.getProperty("Player" + i + "_Type"),
					Boolean.parseBoolean(configFile.getProperty("Player" + i + "_Defeated")),
					Integer.parseInt(configFile.getProperty("Player" + i + "_Team")),
					Integer.parseInt(configFile.getProperty("Player" + i + "_Money")),
					Integer.parseInt(configFile.getProperty("Player" + i + "_Kills")),
					Integer.parseInt(configFile.getProperty("Player" + i + "_Loses")),
					Integer.parseInt(configFile.getProperty("Player" + i + "_Power")),
					Integer.parseInt(configFile.getProperty("Player" + i + "_Using")),//Currently unused
					Boolean.parseBoolean(configFile.getProperty("Player" + i + "_Npc")));//Currently not included
		}
	}
	private void LoadCities(Properties configFile) {
		for (int i = 0; i < edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.builds.size(); i++) {
			edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.list.LoadCity(
					configFile.getProperty("City" + i + "_Type"),
					Integer.parseInt(configFile.getProperty("City" + i + "_Owner")),
					Integer.parseInt(configFile.getProperty("City" + i + "_Health")),
					i);
		}
		for (edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings.Base bld : edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.builds) {
			if (bld.owner!=15) {
				bld.team = edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game.player.get(bld.owner).team;
			}
		}
	}
	private void LoadUnits(Properties configFile) {
		int unitcount = Integer.parseInt(configFile.getProperty("Units"));
		for (int i=0; i < unitcount; i++) {
			Game.list.LoadUnit(
					configFile.getProperty("Unit" + i + "_Type"),
					Integer.parseInt(configFile.getProperty("Unit" + i + "_Owner")),
					Integer.parseInt(configFile.getProperty("Unit" + i + "_Health")),
					Integer.parseInt(configFile.getProperty("Unit" + i + "_Ammo")),
					Integer.parseInt(configFile.getProperty("Unit" + i + "_Fuel")),
					Integer.parseInt(configFile.getProperty("Unit" + i + "_X")),
					Integer.parseInt(configFile.getProperty("Unit" + i + "_Y")),
					Boolean.parseBoolean(configFile.getProperty("Unit" + i + "_Acted")));
		}
	}
}
