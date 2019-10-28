package Model;

import java.util.ArrayList;

import Controller.ControllerBombermanGame;
import View.InfoAgent;
import View.Map;

public class BombermanGame extends Game {

	private ControllerBombermanGame _controllerBombGame;
	
    private ArrayList<Agent> _listAgent;
	private boolean[][] _listBreakableWall;
	private ArrayList<Bombe> _listBombs;
	private ArrayList<Item> _listItems;

	
	public BombermanGame(int maxturn) {
		super(maxturn);
		this._controllerBombGame = new ControllerBombermanGame(this);
	}

	@Override
	public void initialize_game() {
		System.out.println("Le jeu est initialisé");		

		this._listAgent = new ArrayList<Agent>();
		this._listBreakableWall = this._controllerBombGame.getMap().getStart_breakable_walls(); // bug quand on réinit
		this._listBombs = new ArrayList<Bombe>();
		this._listItems = new ArrayList<Item>();
		
		ArrayList<InfoAgent> listAgentInit = this._controllerBombGame.getMap().getStart_agents();
		for(InfoAgent agent : listAgentInit) {
			if(agent.getType() == 'B') {
		    	addBombermanAgent(agent.getX(), agent.getY(), agent.getType());
			}
			else {
				addPNJAgent(agent.getX(), agent.getY(), agent.getType());
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
    	
    	if(this._controllerBombGame.getMap().isWall(newX,newY) || isBreakableWall(newX,newY)
    			|| isAgent(newX, newY)) {
    		return false;
    	}
    	
    	
    	return true;
    }
    
	@Override
	public void takeTurn() {
		
		ArrayList<Bombe> bombeASupprimer = new ArrayList<Bombe>();
				
		for(Bombe bomb : this.getListBomb()) {
			if(bomb.getStateBomb() == StateBomb.Boom) {
				explosionBombe(bomb);
				bombeASupprimer.add(bomb);
			}
			else {
				bomb.changeStateBomb();
			}
		}
		
		//On supprime les bombes qui ont explosé
		for(Bombe bomb : bombeASupprimer) {
			this._listBombs.remove(bomb);
		}
		
		for (Agent agent: this._listAgent) {
			
			AgentAction[] tabAction = {AgentAction.MOVE_UP, AgentAction.MOVE_DOWN, AgentAction.MOVE_LEFT, AgentAction.MOVE_RIGHT, AgentAction.STOP, AgentAction.PUT_BOMB};
			int nbRandom = (int) (Math.random() * tabAction.length);
			AgentAction action = tabAction[nbRandom];
			
			System.out.println(action);
			
			agent.setAction(action);
			
			if(agent.getAction() == AgentAction.PUT_BOMB) {
				if(!isBomb(agent.getX(), agent.getY())) {
					this._listBombs.add(new Bombe(agent.getX(), agent.getY(), 2, StateBomb.Step1));
				}
			}
			else if (agent.getAction() == AgentAction.STOP) {
				// ??
			}
			else {
				if(isLegalMove(agent, agent.getAction())) {
					System.out.println("Deplacement possible");
					agent.moveAgent(action);
				}
			}
			
			System.out.println(agent.getX() + " - " + agent.getY());
		}
   	}
	
	public void explosionBombe(Bombe bomb) {

		// Quand la bombe explose, on detruit les mur cassables et les agents present dans la zone de portée de la bombe
		
		//Pour l'instant la bombe tue son proprietaire => à changer
	
		//Sur la ligne 
		for(int i=bomb.getX() - bomb.getRange(); i<=bomb.getX() + bomb.getRange(); ++i) {
			if(isBreakableWall(i, bomb.getY())) {
				this._listBreakableWall[i][bomb.getY()] = false;
			}
			
			if(isAgent(i, bomb.getY())) {
				this._listAgent.remove(getAgentByCoord(i, bomb.getY()));
			}
		}
		
		//Sur la colonne
		for(int i=bomb.getY() - bomb.getRange(); i<=bomb.getY() + bomb.getRange(); ++i) {
			
			if(isBreakableWall(bomb.getX(), i)) {
				this._listBreakableWall[bomb.getX()][i] = false;
			}
			
			if(isAgent(bomb.getX(), i)) {
				this._listAgent.remove(getAgentByCoord(bomb.getX(), i));
			}
		}		
	}
	
	
	
	public boolean isAgent(int x, int y) {
		for(Agent agent : this._listAgent) {
			if(agent.getX() == x && agent.getY() == y) {
				return true;
			}
		}
		return false;
	}
	
	
	//Verifie si un mur cassable est present sur la case (x;y)
	public boolean isBreakableWall(int x, int y) {
		for(int i=0; i < this._controllerBombGame.getMap().getSizeX(); ++i) {
			if(i == x) {
				for(int j=0; j < this._controllerBombGame.getMap().getSizeY(); ++j) {
					if(j == y) {
						return this._listBreakableWall[i][j];
					}
				}
			}
		}
		return false;
	}
	
	
	public boolean isBomb(int x, int y) {
		for(Bombe bomb : this._listBombs) {
			if(bomb.getX() == x && bomb.getY() == y) {
				return true;
			}
		}
		return false;
	}

	
	public void addBombermanAgent(int pos_x, int pos_y, char type) {
    	AgentFactory agentFactory = new BombermanFactory();
    	this._listAgent.add(agentFactory.createAgent(pos_x, pos_y, type));
    }
	
	
	public void addPNJAgent(int pos_x, int pos_y, char type) {
    	AgentFactory agentFactory = new PNJFactory();
    	this._listAgent.add(agentFactory.createAgent(pos_x, pos_y, type));
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
	
    public ArrayList<Agent> getListAgent() {
    	return this._listAgent;
    }
	
	public boolean[][] getListBreakableWall() {
		return this._listBreakableWall;
	}
	
	public ArrayList<Item> getListItem() {
		return this._listItems;
	}
	
	public ArrayList<Bombe> getListBomb() {
		return this._listBombs;
	}

	public Agent getAgentByCoord(int x, int y) {
		for(Agent agent : this._listAgent) {
			if(agent.getX() == x && agent.getY() == y) {
				return agent;
			}
		}
		return null;
	}
}
