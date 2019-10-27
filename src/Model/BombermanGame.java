package Model;

import java.util.ArrayList;

import Controller.ControllerBombermanGame;
import View.InfoItem;
import View.Map;

public class BombermanGame extends Game {

	ControllerBombermanGame _controllerBombGame;
	
	
	private boolean[][] _listBreakableWall;
	private ArrayList<InfoItem> _listInfoItems;
	private ArrayList<InfoBomb> _listInfoBombs;
	
	
	public BombermanGame(int maxturn) {
		super(maxturn);
		this._controllerBombGame = new ControllerBombermanGame(this);
		

		this._listBreakableWall = this._controllerBombGame.getMap().getStart_breakable_walls();
		this._listInfoItems = new ArrayList<InfoItem>();
		this._listInfoBombs = new ArrayList<InfoBomb>();
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

    public boolean isLegalMove(Agent agent, AgentAction action) {
    	int newX = agent.getX();
    	int newY = agent.getY();
    	switch(action) {
			case MOVE_UP: newY--; break;
			case MOVE_DOWN: newY++; break;
			case MOVE_LEFT: newX--; break;
			case MOVE_RIGHT: newX++; break;
    	}
    	
    	//On verifie si le personnage sort de la map ou non
    	if(newX <= 0 || newX >= this._controllerBombGame.getMap().getSizeX()-1 
    		|| newY <= 0 || newY >= this._controllerBombGame.getMap().getSizeY()-1) {
    		return false;
    	}
    	
    	//On verifie s'il y a un mur, un mur cassable ou un agent sur la nouvelle case
    	if(this._controllerBombGame.getMap().isWall(newX,newY) || this._controllerBombGame.getMap().isBrokableWall(newX,newY)
    			|| this._controllerBombGame.getMap().isAgent(newX, newY)) {
    		return false;
    	}
    	
    	
    	return true;
    }
    
	@Override
	public void takeTurn() {
		for (Agent agent: this.getAgentList()) {
			
			AgentAction[] tabAction = {AgentAction.MOVE_UP, AgentAction.MOVE_DOWN, AgentAction.MOVE_LEFT, AgentAction.MOVE_RIGHT};
			int nbRandom = (int) (Math.random() * 4); // nombre entre 0 et 3
			AgentAction action = tabAction[nbRandom];
			
			System.out.println(action);
				   
			if(isLegalMove(agent, action)) {
				System.out.println("Deplacement possible");
				agent.moveAgent(action);
			}
			
			System.out.println(agent.getX() + " - " + agent.getY());
		}
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
	
	
	public boolean[][] getListBreakableWall() {
		return this._listBreakableWall;
	}
	
	public ArrayList<InfoItem> getListInfoItem() {
		return this._listInfoItems;
	}
	
	public ArrayList<InfoBomb> getListInfoBomb() {
		return this._listInfoBombs;
	}

}
