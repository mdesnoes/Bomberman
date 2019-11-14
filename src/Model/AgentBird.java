package Model;

import Strategy.BirdStrategy;

public class AgentBird extends AgentPNJ {

	public AgentBird(int pos_x, int pos_y, char type, ColorAgent color) {
		super(pos_x, pos_y, type, color);
		
		this.setStrategy(new BirdStrategy());
	}

}
