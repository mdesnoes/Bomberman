package Model;

import Strategy.BirdStrategy;
import org.jetbrains.annotations.NotNull;

public class AgentBird extends AgentPNJ {

	public AgentBird(int pos_x, int pos_y, char type, ColorAgent color) {
		super(pos_x, pos_y, type, color);
		this.setStrategy(new BirdStrategy());
	}
	
	public boolean isLegalMove(BombermanGame bombGame, @NotNull AgentAction action) {
		int newX = this.getX();
    	int newY = this.getY();

    	switch(action) {
			case MOVE_UP:
				newY--;
				break;
			case MOVE_DOWN:
				newY++;
				break;
			case MOVE_LEFT:
				newX--;
				break;
			case MOVE_RIGHT:
				newX++;
				break;
			default:
				break;
    	}
    	
    	//On verifie si l'agent sort de la map ou non
    	if(!bombGame.appartientMap(newX,newY)) {
    		return false;
    	}
    	
    	//Un agent Bird ne peut pas se deplacer sur un autre agent PNJ
		return bombGame.getAgentPNJByCoord(newX, newY) == null;
	}

}
