package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings;

import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings.Base;

public class Airport extends Base {

	public Airport(int owner, int xx, int yy) {
		super(owner, xx, yy);
		name="Airport";
		desc="Creates Air edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units.";
		img = 3;
		Menu = "airport";
	}
}
