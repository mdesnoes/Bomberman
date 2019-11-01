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

	
	private ArrayList<Agent> _listAgentDetruit = new ArrayList<Agent>();
	private ArrayList<Bombe> _listBombeDetruite = new ArrayList<Bombe>();
	
	private final static int TURN_MAX_ITEM = 5;
	
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
    			|| isAgent(newX, newY) || isBomb(newX, newY)) {
    		return false;
    	}
    	
    	
    	return true;
    }
    
	@Override
	public void takeTurn() {
		//Explosion des bombes
		for(Bombe bomb : this._listBombs) {
			Agent agent = this.getAgentByBomb(bomb);
			
			//Si la bombe appartient a un agent (l'agent n'est pas detruit)
			if(agent != null) {
				if(bomb.getStateBomb() == StateBomb.Boom) {
					this.destroyBreakableWall(bomb); // On detruit les murs cassables
					this.destroyOtherAgent(bomb); // On detruit les autres agents
					this._listBombeDetruite.add(agent.getBombe()); //Bombe à supprimer de la liste
					agent.setBombe(null); //L'agent peut reposer une nouvelle bombe
				}
				else {
					//Nouvelle etat de la bombe de l'agent
					Bombe b = agent.getBombe();
					b.changeStateBomb();
					agent.setBombe(b);
				}
			}
			else {
				if(bomb.getStateBomb() == StateBomb.Boom) {
					this.destroyBreakableWall(bomb);
					this.destroyAgent(bomb);
					this._listBombeDetruite.add(bomb); //Bombe à supprimer de la liste
				}
				else {
					bomb.changeStateBomb();
				}
			}
		}
		
		
		//On supprime les agents qui ont été detruit suite à l'explosion des bombes
		for(Agent agent: this._listAgentDetruit) {
			this._listAgent.remove(agent);
		}
		
		//On supprime les bombes qui ont été explosé
		for(Bombe bomb : this._listBombeDetruite) {
			this._listBombs.remove(bomb);
		}
		
			
		//Action des agents restants
		for (Agent agent: this._listAgent) {
			
			
			//Verification des malus/bonus
			if(agent.getIsInvincible()) {
				if(agent.getNbTurnBonusInvincible() >= TURN_MAX_ITEM) {
					agent.setIsInvincible(false);
					agent.setNbTurnBonusInvincible(0);
				}
				else agent.setNbTurnBonusInvincible(agent.getNbTurnBonusInvincible() + 1);
			}
			
			if(agent.getIsSick()) {
				if(agent.getNbTurnMalusSick() >= TURN_MAX_ITEM) {
					agent.setIsSick(false);
					agent.setNbTurnMalusSick(0);
				}
				else agent.setNbTurnMalusSick(agent.getNbTurnMalusSick() + 1);
			}
			
		
			AgentAction[] tabAction = {AgentAction.MOVE_UP, AgentAction.MOVE_DOWN, AgentAction.MOVE_LEFT, AgentAction.MOVE_RIGHT, AgentAction.STOP, AgentAction.PUT_BOMB};
			int nbRandom = (int) (Math.random() * tabAction.length);
			AgentAction action = tabAction[nbRandom];
			
			System.out.println(action);
			
			agent.setAction(action);
			
			if(agent.getAction() == AgentAction.PUT_BOMB) {
				if(agent.getBombe() == null && !agent.getIsSick()) {
					Bombe bombe = new Bombe(agent.getX(), agent.getY(), 2, StateBomb.Step1);
					this._listBombs.add(bombe);
					agent.setBombe(bombe);
				}
			}
			else if (agent.getAction() == AgentAction.STOP) {
				// ??
			}
			else {
				if(isLegalMove(agent, agent.getAction())) {
					System.out.println("Deplacement possible");
					agent.moveAgent(action);
					
					for(Item item : this._listItems) {
						if(agent.getX() == item.getX() && agent.getY() == item.getY()) {
							takeItem(agent, item);
							
						}
					}
				}
			}
			
			System.out.println(agent.getX() + " - " + agent.getY());
		}
		
   	}
	
	
	public void takeItem(Agent agent, Item item) {
		switch(item.getType()) {
			case FIRE_UP: 
				Bombe bombeUp = agent.getBombe();
				bombeUp.setRange(bombeUp.getRange() + 1);
				agent.setBombe(bombeUp);
				break;
			case FIRE_DOWN:
				Bombe bombeDown = agent.getBombe();
				bombeDown.setRange(bombeDown.getRange() - 1);
				agent.setBombe(bombeDown);
				break;	
			case BOMB_UP: break;
	
			case BOMB_DOWN: break;
		
			case FIRE_SUIT: agent.setIsInvincible(true); break;
			case SKULL: agent.setIsSick(true); break;
		}
	}
	
	
	public void destroyBreakableWall(Bombe bomb) {
		//Sur la ligne 
		for(int i=bomb.getX() - bomb.getRange(); i<=bomb.getX() + bomb.getRange(); ++i) {
			if(isBreakableWall(i, bomb.getY())) {
				this._listBreakableWall[i][bomb.getY()] = false;
			}
		}
		
		//Sur la colonne
		for(int i=bomb.getY() - bomb.getRange(); i<=bomb.getY() + bomb.getRange(); ++i) {		
			if(isBreakableWall(bomb.getX(), i)) {
				this._listBreakableWall[bomb.getX()][i] = false;
			}

		}
	}

	
	public void destroyOtherAgent(Bombe bomb) {
		Agent agent = this.getAgentByBomb(bomb);//On recupere l'agent qui a posé la bombe pour ne pas le detruire avec sa propre bombe

		//Sur la ligne 
		for(int i=bomb.getX() - bomb.getRange(); i<=bomb.getX() + bomb.getRange(); ++i) {
			if(i != agent.getX()) {
				if(isAgent(i, bomb.getY())) {
					if(!getAgentByCoord(i, bomb.getY()).getIsInvincible()) {
						this._listAgentDetruit.add(getAgentByCoord(i, bomb.getY()));
					}
				}
			}
		}
		
		//Sur la colonne
		for(int i=bomb.getY() - bomb.getRange(); i<=bomb.getY() + bomb.getRange(); ++i) {
			if(i != agent.getY()) {
				if(isAgent(bomb.getX(), i)) {
					if(!getAgentByCoord(bomb.getX(), i).getIsInvincible()) {
						this._listAgentDetruit.add(getAgentByCoord(bomb.getX(), i));
					}
				}
			}
		}	
	}
	
	public void destroyAgent(Bombe bomb) {
		//Sur la ligne 
		for(int i=bomb.getX() - bomb.getRange(); i<=bomb.getX() + bomb.getRange(); ++i) {
			if(isAgent(i, bomb.getY())) {
				if(!getAgentByCoord(i, bomb.getY()).getIsInvincible()) {
					this._listAgentDetruit.add(getAgentByCoord(i, bomb.getY()));
				}
			}
		}
		
		//Sur la colonne
		for(int i=bomb.getY() - bomb.getRange(); i<=bomb.getY() + bomb.getRange(); ++i) {
			if(isAgent(bomb.getX(), i)) {
				if(!getAgentByCoord(bomb.getX(), i).getIsInvincible()) {
					this._listAgentDetruit.add(getAgentByCoord(bomb.getX(), i));
				}
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
	
	public Agent getAgentByBomb(Bombe bomb) {
		for(Agent agent : this._listAgent) {
			if(agent.getBombe() == bomb) {
				return agent;
			}
		}
		return null;
	}
}
