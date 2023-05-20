package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units;

import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.units.Base;

public class Helecopter extends Base {
	public Helecopter(int owner, int xx, int yy, boolean active) {
		super(owner, xx, yy, active);
		name = "Helecopter";
		nick = "Hele";
		desc = "A fast reaction unit that kills things.";
		img = 4;
		speed = 8;
		legs = Move.FLY;
		ArmorType = Armor.AIR;
		building = "airport";
	}
}
