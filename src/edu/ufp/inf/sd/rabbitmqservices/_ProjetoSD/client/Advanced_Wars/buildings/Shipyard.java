package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings;

//import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.engine.Game;

import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings.Base;

public class Shipyard extends Base {

	public Shipyard(int owner, int xx, int yy) {
		super(owner, xx, yy);
		name="Capital";
		desc="Creates water edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units.";
		img = 4;
		Menu = "shipyard";
		//Game.map.map[yy][xx].swim = true;
	}
}
