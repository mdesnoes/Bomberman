package Model;

import java.util.ArrayList;

import Controller.ControllerBombermanGame;
import Factory.AgentFactory;
import Factory.FactoryProvider;
import Strategy.RandomStrategy;
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
	private final static int PROBABILITE_OBJET = 6;
	
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
			
			
			agentBomberman.executer(this); // Choix de l'action en fonction de la strategie
		}
		
		//On supprime les items ramassés par les agents
		for(Item item : this._listItemsUtilise) {
			this._listItems.remove(item);
		}
		
		
		//Actions des PNJ
		for(AgentPNJ agentPNJ : this._listAgentsPNJ) {
			agentPNJ.executer(this);
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
		
		//Sur la ligne 
		for(int i=bomb.getX() - bomb.getRange(); i<=bomb.getX() + bomb.getRange(); ++i) {
			if(appartientMap(i, bomb.getY())) { // On verifie que les coordonnées appartiennent à la map
				if(this._listBreakableWalls[i][bomb.getY()]) { // On regarde si il y a un mur au coordonnées courante
					this._listBreakableWalls[i][bomb.getY()] = false;
					
					//Probabilité qu'un item apparaisse (1 chance sur 10), tout les items on la meme probabilite
					int nb = (int) (Math.random() * PROBABILITE_OBJET);
					if(nb == 1) {
						ItemType[] tabItem = {ItemType.FIRE_UP,ItemType.FIRE_DOWN,ItemType.BOMB_UP,ItemType.BOMB_DOWN,ItemType.FIRE_SUIT,ItemType.SKULL}; 
						int nbRandom = (int) (Math.random() * tabItem.length);
						Item item = new Item(i, bomb.getY(), tabItem[nbRandom]);
						this._listItems.add(item);
					}
					
				}
			}
		}
		
		//Sur la colonne
		for(int i=bomb.getY() - bomb.getRange(); i<=bomb.getY() + bomb.getRange(); ++i) {
			if(appartientMap(bomb.getX(), i)) {
				if(this._listBreakableWalls[bomb.getX()][i]) {
					this._listBreakableWalls[bomb.getX()][i] = false;
					
					//Probabilité qu'un item apparaisse (1 chance sur 10)
					int nb = (int) (Math.random() * PROBABILITE_OBJET);
					if(nb == 1) {
						ItemType[] tabItem = {ItemType.FIRE_UP,ItemType.FIRE_DOWN,ItemType.BOMB_UP,ItemType.BOMB_DOWN,ItemType.FIRE_SUIT,ItemType.SKULL}; 
						int nbRandom = (int) (Math.random() * tabItem.length);
						Item item = new Item(bomb.getX(), i, tabItem[nbRandom]);
						this._listItems.add(item);
					}
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
	
	
	
	public boolean isBomb(int x, int y) {
		return this.getBombByCoord(x, y) != null;
	}
	
	public Bombe getBombByCoord(int x, int y) {
		for(Bombe bomb : this._listBombs) {
			if(bomb.getX() == x && bomb.getY() == y) {
				return bomb;
			}
		}
		return null;
	}
	
	// retourne vrai si les coordonnées appartient à la map
	public boolean appartientMap(int x, int y) {		
		return x >=0 && x <= this._controllerBombGame.getMap().getSizeX()-1
				&& y>=0 && y <= this._controllerBombGame.getMap().getSizeY()-1;
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
	
	public void addListBombs(Bombe bomb) {
		this._listBombs.add(bomb);
	}
	
	public void addListItemUtilise(Item item) {
		this._listItemsUtilise.add(item);
	}
	
	public void removeAgentBomberman(AgentBomberman bomberman) {
	    this._listAgentsBomberman.remove(bomberman);

	}
	
	public ControllerBombermanGame getControllerBombGame() {
		return this._controllerBombGame;
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
