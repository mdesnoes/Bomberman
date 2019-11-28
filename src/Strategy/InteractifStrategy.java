package Strategy;


import Model.Agent;
import Model.AgentAction;
import Model.BombermanGame;
import View.ViewModeInteractif;


public class InteractifStrategy implements Strategy {	

	@Override
	public AgentAction chooseAction(BombermanGame bombermanGame, Agent agent) {
		
		ViewModeInteractif viewModeInteractif = ViewModeInteractif.getInstance();
		viewModeInteractif.addKeyListener(viewModeInteractif);

		viewModeInteractif.requestFocus();
		System.out.println("Action bomberman " + agent.getColor() + " : " + viewModeInteractif.getAction());
		
		agent.setAction(viewModeInteractif.getAction());
		
		return viewModeInteractif.getAction();
	}
	
}
