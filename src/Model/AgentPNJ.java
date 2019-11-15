package Model;


public abstract class AgentPNJ extends Agent {

	public AgentPNJ(int pos_x, int pos_y, char type, ColorAgent color) {
		super(pos_x, pos_y, type, color);
	}
	
	public void executer(BombermanGame bombermanGame) {
		
		AgentAction action = this.getStrategy().chooseAction(bombermanGame, this);
		
		if(action == AgentAction.MOVE_UP || action == AgentAction.MOVE_DOWN 
				|| action == AgentAction.MOVE_LEFT || action == AgentAction.MOVE_RIGHT) {
			if(this.isLegalMove(bombermanGame, action)) {
				this.moveAgent(action);
				
				AgentBomberman agentBomberman = bombermanGame.getAgentBombermanByCoord(this.getX(), this.getY());
				if(agentBomberman != null) {
					bombermanGame.removeAgentBomberman(agentBomberman);
				}
			}
		}
		
		//this.setAction(action);
	}
	

	public void moveAgent(AgentAction action) {
		
		switch(action) {
			case MOVE_UP: this.setY(this.getY() - 1); break;
			case MOVE_DOWN: this.setY(this.getY() + 1); break;
			case MOVE_LEFT: this.setX(this.getX() - 1); break;
			case MOVE_RIGHT: this.setX(this.getX() + 1); break;
			case STOP: break;
			case PUT_BOMB: break;
		}
	}
	
	public boolean isLegalMove(BombermanGame bombGame, AgentAction action) {
		int newX = this.getX();
    	int newY = this.getY();
    	switch(action) {
			case MOVE_UP: newY--; break;
			case MOVE_DOWN: newY++; break;
			case MOVE_LEFT: newX--; break;
			case MOVE_RIGHT: newX++; break;
			default: break;
    	}
    	
    	//On verifie si l'agent sort de la map ou non
    	if(!bombGame.appartientMap(newX,newY)) {
    		return false;
    	}
    	
    	//On verifie s'il y a un mur, un mur cassable ou une bombe sur la nouvelle case
    	if(bombGame.getControllerBombGame().getMap().get_walls()[newX][newY] || bombGame.getListBreakableWall()[newX][newY] || bombGame.isBomb(newX, newY)) {
    		return false;
    	}
    	
    	//Un agent PNJ ne peut pas se deplacer sur un autre agent PNJ
    	if(bombGame.getAgentPNJByCoord(newX, newY) != null) {
    		return false;
    	}
    	
    	return true;
	}

}
