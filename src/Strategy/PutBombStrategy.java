package Strategy;

import Model.Agent;
import Model.AgentAction;
import Model.AgentBomberman;
import Model.BombermanGame;

// Strategy pour les bomberman
// S'il y a un agent dans le rayon de ça bombe, il pose une bombe, sinon il se deplace avec la RaijonStrategy

public class PutBombStrategy implements Strategy {
	
	EsquiveStrategy esquiveStrategy = new EsquiveStrategy();
	RaijonStrategy raijonStrategy = new RaijonStrategy();

	@Override
	public AgentAction chooseAction(BombermanGame bombermanGame, Agent agent) {

		AgentBomberman agentBomberman = (AgentBomberman) agent;
		
		for(int i=agentBomberman.getX() - agentBomberman.getRangeBomb(); i<=agentBomberman.getX() + agentBomberman.getRangeBomb(); ++i) {
			for(int j=agentBomberman.getY() - agentBomberman.getRangeBomb(); j<=agentBomberman.getY() + agentBomberman.getRangeBomb(); ++j) {
				if(i != agent.getX() && j != agent.getY()) {
					if(bombermanGame.getAgentBombermanByCoord(i, j) != null) {
						if(agentBomberman.canPutBomb()) {
							return AgentAction.PUT_BOMB;
						}
						else { break; }
					}
				}
			}
		}
		
		return raijonStrategy.chooseAction(bombermanGame, agent);
	}

}
