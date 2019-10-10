package Model;

import java.util.ArrayList;

import Controller.ControllerBombermanGame;
import View.Map;

public class BombermanGame extends Game {

	ControllerBombermanGame _controllerBombGame;
	
	public BombermanGame(int maxturn) {
		super(maxturn);
		this._controllerBombGame = new ControllerBombermanGame(this);
	}

	@Override
	public void initialize_game() {
		System.out.println("Le jeu est initialisé");		

		Map map_init = this._controllerBombGame.getMap();
		
		ArrayList<InfoAgent> listAgentInit = map_init.getStart_agents();
		
		for(InfoAgent agent : listAgentInit) {
			if(agent.getType() == 'B') {
				super.addBombermanAgent(agent.getX(), agent.getY(), agent.getType());
			}
			else {
				super.addPNJAgent(agent.getX(), agent.getY(), agent.getType());
			}
			
			System.out.println(agent.getX() + " - " + agent.getY() + " type : " + agent.getType());
		}
		
	}

	@Override
	public void takeTurn() {
		System.out.println("Tour " + this.getTurn() + " du jeu en cours");		
	}

	@Override
	public boolean gameContinue() {
		System.out.println("Le jeu continue");
		return true;
	}

	@Override
	public void gameOver() {
		System.out.println("Fin du jeu");		
	}

}
