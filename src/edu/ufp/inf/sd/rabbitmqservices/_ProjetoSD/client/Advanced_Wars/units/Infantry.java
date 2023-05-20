package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units;

import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units.Base;

public class Infantry extends Base {
	public Infantry(int owner, int xx, int yy, boolean active) {
		super(owner, xx, yy, active);
		name = "Infantry";
		nick = "Inf";
		desc = "Weakest edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units here.";
		img = 0;
		speed = 4;
		raider = true;
	}
}
