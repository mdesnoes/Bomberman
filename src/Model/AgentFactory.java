package Model;

import Strategy.Strategy;

public interface AgentFactory {
		
	public Agent createAgent(int pos_x, int pos_y, char type);
	
}
