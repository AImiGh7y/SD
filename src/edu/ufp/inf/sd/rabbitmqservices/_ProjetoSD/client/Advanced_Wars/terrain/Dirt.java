package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.terrain;

import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.terrain.Base;

public class Dirt extends Base {
	public Dirt() {
		name = "Dirt";
	}
	public double speed() {return 1;}
	public double defense() {return 1;}
}
