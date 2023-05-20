package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings;

import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings.Base;

public class Barracks extends Base {

	public Barracks(int owner, int xx, int yy) {
		super(owner, xx, yy);
		name="Barracks";
		desc="Creates ground edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units.";
		img = 2;
		Menu = "barracks";
	}
}
