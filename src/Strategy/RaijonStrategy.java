package Strategy;

import Model.Agent;
import Model.AgentAction;
import Model.AgentBomberman;
import Model.BombermanGame;

public class RaijonStrategy implements Strategy {

	@Override
	public AgentAction chooseAction(BombermanGame bombermanGame, Agent agent) {
        
        int procheDist = 1000;
        AgentBomberman procheBomb = null;
        for (AgentBomberman i : bombermanGame.getListAgentBomberman()) {
            int dist = Math.abs(agent.getX() - i.getX()) + Math.abs(agent.getY() - i.getY());
            if (dist < procheDist) {
                procheDist = dist;
                procheBomb = i;
            }
        }
        if (Math.abs(agent.getX() - procheBomb.getX()) >= Math.abs(agent.getY() - procheBomb.getY())) {
            if (agent.getX() < procheBomb.getX()) {
                return AgentAction.MOVE_RIGHT;
            } else {
                return AgentAction.MOVE_LEFT;
            }
        } else {
            if (agent.getY() < procheBomb.getY()) {
                return AgentAction.MOVE_DOWN;
            } else {
                return AgentAction.MOVE_UP;
            }
        }
    
	}

}
