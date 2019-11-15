package Model;

import Strategy.RaijonStrategy;

public class AgentRajion extends AgentPNJ {

	public AgentRajion(int pos_x, int pos_y, char type, ColorAgent color) {
		super(pos_x, pos_y, type, color);
		
		this.setStrategy(new RaijonStrategy());
	}

}
