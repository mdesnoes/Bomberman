package Model;

import java.util.ArrayList;

import Controller.ControllerBombermanGame;
import View.InfoAgent;
import View.Map;

public class BombermanGame extends Game {

	private ControllerBombermanGame _controllerBombGame;
	
    private ArrayList<Agent> _listAgents;
	private boolean[][] _listBreakableWalls;
	private ArrayList<Bombe> _listBombs;
	private ArrayList<Item> _listItems;

	
	private ArrayList<Agent> _listAgentsDetruit = new ArrayList<Agent>();
	private ArrayList<Bombe> _listBombesDetruite = new ArrayList<Bombe>();
	private ArrayList<Item> _listItemsUtilise = new ArrayList<Item>();

	
	private final static int TURN_MAX_ITEM = 5;
	
	public BombermanGame(int maxturn) {
		super(maxturn);
		this._controllerBombGame = new ControllerBombermanGame(this);
	}

	@Override
	public void initialize_game() {
		System.out.println("Le jeu est initialisé");		

		this._listAgents = new ArrayList<Agent>();
		this._listBreakableWalls = this._controllerBombGame.getMap().getStart_breakable_walls(); // bug quand on réinit
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
		
		this._listAgentsDetruit = new ArrayList<Agent>();
		this._listBombesDetruite = new ArrayList<Bombe>();
		this._listItemsUtilise = new ArrayList<Item>();
		
		//Explosion des bombes
		for(Bombe bomb : this._listBombs) {
			Agent agent = this.getAgentByBomb(bomb);
			
			//Si la bombe appartient a un agent (l'agent n'est pas detruit)
			if(agent != null) {
				if(bomb.getStateBomb() == StateBomb.Boom) {					
					this.destroyBreakableWall(bomb); // On detruit les murs cassables
					this.destroyOtherAgent(bomb); // On detruit les autres agents
					this._listBombesDetruite.add(bomb); //Bombe à supprimer de la liste
					agent.removeBombe(bomb); //L'agent peut reposer une nouvelle bombe
				}
				else {
					//Nouvelle etat des bombes de l'agent
					ArrayList<Bombe> listBombNewState = new ArrayList<Bombe>();
					for(Bombe bombAgent : agent.getListBombe()) {
						bombAgent.changeStateBomb();
						listBombNewState.add(bombAgent);
					}
					agent.setListBombe(listBombNewState);
				}
			}
			else {
				if(bomb.getStateBomb() == StateBomb.Boom) {
					this.destroyBreakableWall(bomb);
					this.destroyAgent(bomb);
					this._listBombesDetruite.add(bomb); //Bombe à supprimer de la liste
				}
				else {
					bomb.changeStateBomb();
				}
			}
		}
		
		
		//On supprime les agents qui ont été detruit suite à l'explosion des bombes
		for(Agent agent: this._listAgentsDetruit) {
			this._listAgents.remove(agent);
		}
		
		//On supprime les bombes qui ont été explosé
		for(Bombe bomb : this._listBombesDetruite) {
			this._listBombs.remove(bomb);
		}
		
			
		//Action des agents restants
		for (Agent agent: this._listAgents) {			
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
				if(agent.canPutBomb()) {
					Bombe bombe = new Bombe(agent.getX(), agent.getY(), agent.getRangeBomb(), StateBomb.Step1);
					this._listBombs.add(bombe);
					agent.addBombe(bombe);
				}
			}
			else if (agent.getAction() == AgentAction.STOP) {
				// ??
			}
			else {
				if(isLegalMove(agent, agent.getAction())) {
					agent.moveAgent(action);
					
					for(Item item : this._listItems) {
						if(agent.getX() == item.getX() && agent.getY() == item.getY()) {
							takeItem(agent, item);
							_listItemsUtilise.add(item);
						}
					}
				}
			}

		}
		
		//On supprime les items ramassé par les agents
		for(Item item : this._listItemsUtilise) {
			this._listItems.remove(item);
		}
		
   	}
	
	
	public void takeItem(Agent agent, Item item) {
		switch(item.getType()) {
			case FIRE_UP:
				agent.setRangeBomb(agent.getRangeBomb() + 1);
				
				//On met a jour les bombes déjà poser sur le terrain
				ArrayList<Bombe> listBombNewRangeUp = new ArrayList<Bombe>();
				for(Bombe bombAgent : agent.getListBombe()) {
					bombAgent.setRange(agent.getRangeBomb());
					listBombNewRangeUp.add(bombAgent);
				}
				agent.setListBombe(listBombNewRangeUp);
				break;
			case FIRE_DOWN:
				agent.setRangeBomb(agent.getRangeBomb() - 1);

				//On met a jour les bombes déjà poser sur le terrain
				ArrayList<Bombe> listBombNewRangeDown = new ArrayList<Bombe>();
				for(Bombe bombAgent : agent.getListBombe()) {
					bombAgent.setRange(agent.getRangeBomb());
					listBombNewRangeDown.add(bombAgent);
				}
				agent.setListBombe(listBombNewRangeDown);
				break;	
			case BOMB_UP: agent.setNbBombe(agent.getNbBombe() + 1); break;
			case BOMB_DOWN: agent.setNbBombe(agent.getNbBombe() - 1); break;
			case FIRE_SUIT: agent.setIsInvincible(true); break;
			case SKULL: agent.setIsSick(true); break;
		}
	}
	
	
	public void destroyBreakableWall(Bombe bomb) {
		int probabilite = 6;
		
		//Sur la ligne 
		for(int i=bomb.getX() - bomb.getRange(); i<=bomb.getX() + bomb.getRange(); ++i) {
			if(isBreakableWall(i, bomb.getY())) {
				this._listBreakableWalls[i][bomb.getY()] = false;
				
				//Probabilité qu'un item apparaisse (1 chance sur 10), tout les items on la meme probabilite
				int nb = (int) (Math.random() * probabilite);
				if(nb == 1) {
					ItemType[] tabItem = {ItemType.FIRE_UP,ItemType.FIRE_DOWN,ItemType.BOMB_UP,ItemType.BOMB_DOWN,ItemType.FIRE_SUIT,ItemType.SKULL}; 
					int nbRandom = (int) (Math.random() * tabItem.length);
					Item item = new Item(i, bomb.getY(), tabItem[nbRandom]);
					this._listItems.add(item);
				}
				
			}
		}
		
		//Sur la colonne
		for(int i=bomb.getY() - bomb.getRange(); i<=bomb.getY() + bomb.getRange(); ++i) {		
			if(isBreakableWall(bomb.getX(), i)) {
				this._listBreakableWalls[bomb.getX()][i] = false;
				
				//Probabilité qu'un item apparaisse (1 chance sur 10)
				int nb = (int) (Math.random() * probabilite);
				if(nb == 1) {
					ItemType[] tabItem = {ItemType.FIRE_UP,ItemType.FIRE_DOWN,ItemType.BOMB_UP,ItemType.BOMB_DOWN,ItemType.FIRE_SUIT,ItemType.SKULL}; 
					int nbRandom = (int) (Math.random() * tabItem.length);
					Item item = new Item(bomb.getX(), i, tabItem[nbRandom]);
					this._listItems.add(item);
				}
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
						this._listAgentsDetruit.add(getAgentByCoord(i, bomb.getY()));
					}
				}
			}
		}
		
		//Sur la colonne
		for(int i=bomb.getY() - bomb.getRange(); i<=bomb.getY() + bomb.getRange(); ++i) {
			if(i != agent.getY()) {
				if(isAgent(bomb.getX(), i)) {
					if(!getAgentByCoord(bomb.getX(), i).getIsInvincible()) {
						this._listAgentsDetruit.add(getAgentByCoord(bomb.getX(), i));
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
					this._listAgentsDetruit.add(getAgentByCoord(i, bomb.getY()));
				}
			}
		}
		
		//Sur la colonne
		for(int i=bomb.getY() - bomb.getRange(); i<=bomb.getY() + bomb.getRange(); ++i) {
			if(isAgent(bomb.getX(), i)) {
				if(!getAgentByCoord(bomb.getX(), i).getIsInvincible()) {
					this._listAgentsDetruit.add(getAgentByCoord(bomb.getX(), i));
				}
			}
		}	
	}

	
	
	public boolean isAgent(int x, int y) {
		for(Agent agent : this._listAgents) {
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
						return this._listBreakableWalls[i][j];
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
    	this._listAgents.add(agentFactory.createAgent(pos_x, pos_y, type));
    }
	
	
	public void addPNJAgent(int pos_x, int pos_y, char type) {
    	AgentFactory agentFactory = new PNJFactory();
    	this._listAgents.add(agentFactory.createAgent(pos_x, pos_y, type));
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
    	return this._listAgents;
    }
	
	public boolean[][] getListBreakableWall() {
		return this._listBreakableWalls;
	}
	
	public ArrayList<Item> getListItem() {
		return this._listItems;
	}
	
	public ArrayList<Bombe> getListBomb() {
		return this._listBombs;
	}

	public Agent getAgentByCoord(int x, int y) {
		for(Agent agent : this._listAgents) {
			if(agent.getX() == x && agent.getY() == y) {
				return agent;
			}
		}
		return null;
	}
	
	public Agent getAgentByBomb(Bombe bomb) {
		for(Agent agent : this._listAgents) {
			for(Bombe bombAgent : agent.getListBombe()) {
				if(bombAgent == bomb) {
					return agent;
				}
			}
		}
		return null;
	}
}
