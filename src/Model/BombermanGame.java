package Model;

import java.util.ArrayList;

import Controller.ControllerBombermanGame;
import View.InfoAgent;
import View.Map;

public class BombermanGame extends Game {

	private ControllerBombermanGame _controllerBombGame;
	
    private ArrayList<AgentBomberman> _listAgentsBomberman;
    private ArrayList<AgentPNJ> _listAgentsPNJ;
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

		this._listAgentsBomberman = new ArrayList<AgentBomberman>();
		this._listAgentsPNJ = new ArrayList<AgentPNJ>();
		this._listBreakableWalls = this._controllerBombGame.getMap().getStart_breakable_walls(); // bug quand on réinit
		this._listBombs = new ArrayList<Bombe>();
		this._listItems = new ArrayList<Item>();
		
		ArrayList<InfoAgent> listAgentInit = this._controllerBombGame.getMap().getStart_agents();
		for(InfoAgent agent : listAgentInit) {
			AgentFactory agentFactory = FactoryProvider.getFactory(agent.getType());
			
			if(agent.getType() == 'B') {
			    this._listAgentsBomberman.add((AgentBomberman) agentFactory.createAgent(agent.getX(), agent.getY(), agent.getType()));
			}
			else {
		    	this._listAgentsPNJ.add((AgentPNJ) agentFactory.createAgent(agent.getX(), agent.getY(), agent.getType()));
			}
			
			System.out.println(agent.getX() + " - " + agent.getY() + " type : " + agent.getType());
		}
	}

    public boolean isLegalMove(Agent agent) {
    	int newX = agent.getX();
    	int newY = agent.getY();
    	switch(agent.getAction()) {
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
    	
    	if(this._controllerBombGame.getMap().isWall(newX,newY) || isBreakableWall(newX,newY) || isBomb(newX, newY)) {
    		return false;
    	}
    	
    	 //Un agent bomberman ne peut pas se deplacer sur un autre agent
    	if(agent instanceof AgentBomberman) {
			if(this.getAgentByCoord(newX, newY) != null) {
				return false;
			} 
		}
    	
    	//Un agent PNJ peut se deplacer sur un agent bomberman mais pas sur un autres agent PNJ
    	if(agent instanceof AgentPNJ) {
    		if(this.getAgentPNJByCoord(newX, newY) != null) {
    			return false;
    		}
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
			AgentBomberman agentBomberman = this.getAgentBombermanByBomb(bomb);
			
			//Si la bombe appartient a un agent (l'agent n'est pas detruit)
			if(agentBomberman != null) {
				if(bomb.getStateBomb() == StateBomb.Boom) {					
					this.destroyBreakableWall(bomb); // On detruit les murs cassables
					this.destroyOtherAgent(bomb); // On detruit les autres agents
					this._listBombesDetruite.add(bomb); //Bombe à supprimer de la liste
					agentBomberman.removeBombe(bomb); //L'agent peut reposer une nouvelle bombe
				}
				else {
					//Nouvelle etat des bombes de l'agent
					ArrayList<Bombe> listBombNewState = new ArrayList<Bombe>();
					for(Bombe bombAgent : agentBomberman.getListBombe()) {
						bombAgent.changeStateBomb();
						listBombNewState.add(bombAgent);
					}
					agentBomberman.setListBombe(listBombNewState);
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
			if(agent instanceof AgentBomberman) {
				this._listAgentsBomberman.remove(agent);
			}
			else if(agent instanceof AgentPNJ){
				this._listAgentsPNJ.remove(agent);
			}
		}
		
		//On supprime les bombes qui ont été explosé
		for(Bombe bomb : this._listBombesDetruite) {
			this._listBombs.remove(bomb);
		}
		
			
		//Action des agents bomberman
		for (AgentBomberman agentBomberman: this._listAgentsBomberman) {			
			//Verification des malus/bonus des bomberman
			if(agentBomberman.getIsInvincible()) {
				if(agentBomberman.getNbTurnBonusInvincible() >= TURN_MAX_ITEM) {
					agentBomberman.setIsInvincible(false);
					agentBomberman.setNbTurnBonusInvincible(0);
				}
				else agentBomberman.setNbTurnBonusInvincible(agentBomberman.getNbTurnBonusInvincible() + 1);
			}
			
			if(agentBomberman.getIsSick()) {
				if(agentBomberman.getNbTurnMalusSick() >= TURN_MAX_ITEM) {
					agentBomberman.setIsSick(false);
					agentBomberman.setNbTurnMalusSick(0);
				}
				else agentBomberman.setNbTurnMalusSick(agentBomberman.getNbTurnMalusSick() + 1);
			}
			
			
			AgentAction[] tabAction = {AgentAction.MOVE_UP, AgentAction.MOVE_DOWN, AgentAction.MOVE_LEFT, AgentAction.MOVE_RIGHT, AgentAction.STOP, AgentAction.PUT_BOMB};
			int nbRandom = (int) (Math.random() * tabAction.length);
			AgentAction action = tabAction[nbRandom];
						
			
			System.out.println(action);
			agentBomberman.setAction(action);
			
			if(agentBomberman.getAction() == AgentAction.PUT_BOMB) {
				if(agentBomberman.canPutBomb()) {
					Bombe bombe = new Bombe(agentBomberman.getX(), agentBomberman.getY(), agentBomberman.getRangeBomb(), StateBomb.Step1);
					this._listBombs.add(bombe);
					agentBomberman.addBombe(bombe);
				}
			}
			else if (agentBomberman.getAction() == AgentAction.STOP) {
				// ??
			}
			else {
				if(isLegalMove(agentBomberman)) {
					agentBomberman.moveAgent(action);
					
					for(Item item : this._listItems) {
						if(agentBomberman.getX() == item.getX() && agentBomberman.getY() == item.getY()) {
							takeItem(agentBomberman, item);
							this._listItemsUtilise.add(item);
						}
					}
				}
			}

		}
		
		//On supprime les items ramassé par les agents
		for(Item item : this._listItemsUtilise) {
			this._listItems.remove(item);
		}
		
		
		//Actions des PNJ
		for(AgentPNJ agentPNJ : this._listAgentsPNJ) {
			AgentAction[] tabAction = {AgentAction.MOVE_UP, AgentAction.MOVE_DOWN, AgentAction.MOVE_LEFT, AgentAction.MOVE_RIGHT, AgentAction.STOP};
			int nbRandom = (int) (Math.random() * tabAction.length);
			AgentAction action = tabAction[nbRandom];
			
			agentPNJ.setAction(action);
			
			if (agentPNJ.getAction() == AgentAction.STOP) {
				// ??
			}
			else {
				if(isLegalMove(agentPNJ)) {
					agentPNJ.moveAgent(action);
					
					AgentBomberman agentBomberman = this.getAgentBombermanByCoord(agentPNJ.getX(), agentPNJ.getY());
					if(agentBomberman != null) {
						this._listAgentsBomberman.remove(agentBomberman);
					}
				}
			}
		}
		
   	}
	
	
	public void takeItem(AgentBomberman agent, Item item) {
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
		AgentBomberman agentBomberman = this.getAgentBombermanByBomb(bomb);//On recupere l'agent qui a posé la bombe pour ne pas le detruire avec sa propre bombe

		//Sur la ligne 
		for(int i=bomb.getX() - bomb.getRange(); i<=bomb.getX() + bomb.getRange(); ++i) {
			if(i != agentBomberman.getX()) {
				Agent agent = getAgentByCoord(i, bomb.getY());

				if(agent != null) { // Si on trouve un agent
					if(agent instanceof AgentBomberman) {
						if(!((AgentBomberman) agent).getIsInvincible()) {
							this._listAgentsDetruit.add(agent);
						}
					}
					else {
						this._listAgentsDetruit.add(agent);
					}
				}
			}
		}
		
		//Sur la colonne
		for(int i=bomb.getY() - bomb.getRange(); i<=bomb.getY() + bomb.getRange(); ++i) {
			if(i != agentBomberman.getY()) {
				Agent agent = getAgentByCoord(bomb.getX(), i);

				if(agent != null) {
					if(agent instanceof AgentBomberman) {
						if(!((AgentBomberman) agent).getIsInvincible()) {
							this._listAgentsDetruit.add(agent);
						}
					}
					else {
						this._listAgentsDetruit.add(agent);
					}
				}
			}
		}	
	}

	
	public void destroyAgent(Bombe bomb) {
		//Sur la ligne 
		for(int i=bomb.getX() - bomb.getRange(); i<=bomb.getX() + bomb.getRange(); ++i) {
			Agent agent = getAgentByCoord(i, bomb.getY()); //On recupere l'agent trouvé

			if(agent != null) {
				if(agent instanceof AgentBomberman) {
					if(!((AgentBomberman) agent).getIsInvincible()) {
						this._listAgentsDetruit.add(agent);
					}
				}
				else {
					this._listAgentsDetruit.add(agent);
				}
			}
		}
		
		//Sur la colonne
		for(int i=bomb.getY() - bomb.getRange(); i<=bomb.getY() + bomb.getRange(); ++i) {
			Agent agent = getAgentByCoord(bomb.getX(), i); //On recupere l'agent trouvé

			if(agent != null) {
				if(agent instanceof AgentBomberman) {
					if(!((AgentBomberman) agent).getIsInvincible()) {
						this._listAgentsDetruit.add(agent);
					}
				}
				else {
					this._listAgentsDetruit.add(agent);
				}
			}
		}	
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

	
    public ArrayList<AgentBomberman> getListAgentBomberman() {
    	return this._listAgentsBomberman;
    }
    
    public ArrayList<AgentPNJ> getListAgentPNJ() {
    	return this._listAgentsPNJ;
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
	
	public AgentPNJ getAgentPNJByCoord(int x, int y) {
		for(AgentPNJ agent : this._listAgentsPNJ) {
			if(agent.getX() == x && agent.getY() == y) {
				return agent;
			}
		}
		return null;
	}
	

	public AgentBomberman getAgentBombermanByCoord(int x, int y) {
		for(AgentBomberman agent : this._listAgentsBomberman) {
			if(agent.getX() == x && agent.getY() == y) {
				return agent;
			}
		}
		return null;
	}
	
	public Agent getAgentByCoord(int x, int y) {
		AgentBomberman agentBomberman = this.getAgentBombermanByCoord(x, y);
		AgentPNJ agentPNJ = this.getAgentPNJByCoord(x, y);
		
		if(agentBomberman != null) {
			return agentBomberman;
		}
		else if(agentPNJ != null) {
			return agentPNJ;
		}
		return null;
	}

	
	
	public AgentBomberman getAgentBombermanByBomb(Bombe bomb) {
		for(AgentBomberman agent : this._listAgentsBomberman) {
			for(Bombe bombAgent : agent.getListBombe()) {
				if(bombAgent == bomb) {
					return agent;
				}
			}
		}
		return null;
	}
	
	
	@Override
	public boolean gameContinue() {
		return this._listAgentsBomberman.size() > 1;
	}

	@Override
	public void gameOver() {
		System.out.println("Fin du jeu");
		
		if(this._listAgentsBomberman.size() <= 0) {
			System.out.println("Victoire des agents PNJ !");
		}
		else if(this._listAgentsBomberman.size() == 1) {
			System.out.println("Victoire de l'agent '" + this._listAgentsBomberman.get(0).getColor() + "'");
		}
	}
}
