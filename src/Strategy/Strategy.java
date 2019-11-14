package Strategy;

import Model.Agent;
import Model.AgentAction;
import Model.BombermanGame;

public interface Strategy {
	
	public AgentAction chooseAction(BombermanGame bombermanGame, Agent agent);
}
