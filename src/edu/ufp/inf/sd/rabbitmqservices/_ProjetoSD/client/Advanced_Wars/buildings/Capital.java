package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings;

import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.buildings.Base;

public class Capital extends Base {

	public Capital(int owner, int xx, int yy) {
		super(owner, xx, yy);
		name="Capital";
		desc="Don't lose this territory, or else you lose!";
		img = 0;
	}
}
