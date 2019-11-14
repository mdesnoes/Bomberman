package Strategy;

import Model.Agent;
import Model.AgentAction;
import Model.BombermanGame;

// L'agent avance tout droit et quand il rencontre un mur, il tourne a droite ou à gauche

public class BasiqueStrategy implements Strategy {

	@Override
	public AgentAction chooseAction(BombermanGame bombermanGame, Agent agent) {
		
		if(bombermanGame.isLegalMove(agent)) {
			
		}
		
		return null;
	}

}
