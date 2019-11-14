package Model;

import Strategy.RandomStrategy;

public class AgentEnnemyBasique extends AgentPNJ {

	public AgentEnnemyBasique(int pos_x, int pos_y, char type, ColorAgent color) {
		super(pos_x, pos_y, type, color);
		
		this.setStrategy(new RandomStrategy());
	}


}
