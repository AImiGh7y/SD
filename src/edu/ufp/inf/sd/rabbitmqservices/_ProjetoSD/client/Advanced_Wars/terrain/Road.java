package edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.terrain;

import edu.ufp.inf.sd.rabbitmqservices._ProjetoSD.client.Advanced_Wars.terrain.Base;

public class Road extends Base {
	public Road() {
		name = "Road";
		oldx = x = 5;
		oldy = y = 0;
	}
	public double speed() {return 1.1;}
	public double defense() {return 1;}
}
