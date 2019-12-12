package Model;

import Controller.ControllerBombermanGame;
import Factory.AgentFactory;
import Factory.FactoryProvider;
import Strategy.*;
import View.InfoAgent;
import View.ViewGagnant;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

public class BombermanGame extends Game {

	private ModeJeu mode;
	private ControllerBombermanGame _controllerBombGame;
    private ArrayList<AgentBomberman> _listAgentsBomberman;
    private ArrayList<AgentPNJ> _listAgentsPNJ;
	private boolean[][] _listBreakableWalls;
	private ArrayList<Bombe> _listBombs;
	private ArrayList<Item> _listItems;
	private ArrayList<Agent> _listAgentsDetruit = new ArrayList<>();
	private ArrayList<Bombe> _listBombesDetruite = new ArrayList<>();
	private ArrayList<Item> _listItemsUtilise = new ArrayList<>();
	private final static int TURN_MAX_ITEM = 5;
	private final static int PROBABILITE_OBJET = 6;
	private int reward = 0;

	public BombermanGame(ModeJeu mode, int maxturn) {
		super(maxturn);
		this.mode = mode;
		this._controllerBombGame = new ControllerBombermanGame(this);
	}

	public void initialize_game() {
		System.out.println("Le jeu est initialisé");
		this._listAgentsBomberman = new ArrayList<>();
		this._listAgentsPNJ = new ArrayList<>();
		this._listBreakableWalls = this._controllerBombGame.getMap().getStart_breakable_walls(); // bug quand on réinit
		this._listBombs = new ArrayList<>();
		this._listItems = new ArrayList<>();
		ArrayList<InfoAgent> listAgentInit = this._controllerBombGame.getMap().getStart_agents();

		for(InfoAgent agent : listAgentInit) {
			AgentFactory agentFactory = FactoryProvider.getFactory(agent.getType());

			if(agent.getType() == 'B') {
			    this._listAgentsBomberman.add((AgentBomberman) agentFactory.createAgent(agent.getX(), agent.getY(), agent.getType(), new PutBombStrategy()));
			}
			else {
				Strategy strategy = null;
				if(this.mode == ModeJeu.PERCEPTRON) {
					strategy = new RandomStrategy();
				}
		    	this._listAgentsPNJ.add((AgentPNJ) agentFactory.createAgent(agent.getX(), agent.getY(), agent.getType(), strategy));
			}

			System.out.println(agent.getX() + " - " + agent.getY() + " type : " + agent.getType());
		}
		
		//En mode solo, on contrôle le premier agent
		if(this.mode == ModeJeu.SOLO) {
			this._listAgentsBomberman.get(0).setStrategy(new InteractifStrategyCommande1());
		}
		//En mode duo ou duel, on peut controler les deux premiers agents (avec des touches différentes)
		else if(this.mode == ModeJeu.DUO || this.mode == ModeJeu.DUEL) {
			this._listAgentsBomberman.get(0).setStrategy(new InteractifStrategyCommande1());
			this._listAgentsBomberman.get(1).setStrategy(new InteractifStrategyCommande2());
		}
		
	}

	public void takeTurn() {
		this._listAgentsDetruit = new ArrayList<>();
		this._listBombesDetruite = new ArrayList<>();
		this._listItemsUtilise = new ArrayList<>();

		//Action des agents bomberman
		for (AgentBomberman agentBomberman: this._listAgentsBomberman) {
			//Verification des malus/bonus des bomberman
			if(agentBomberman.isInvincible()) {
				if(agentBomberman.getNbTurnBonusInvincible() >= TURN_MAX_ITEM) {
					agentBomberman.setIsInvincible(false);
					agentBomberman.setNbTurnBonusInvincible(0);
				} else {
					agentBomberman.setNbTurnBonusInvincible(agentBomberman.getNbTurnBonusInvincible() + 1);
				}
			}

			if(agentBomberman.getIsSick()) {
				if(agentBomberman.getNbTurnMalusSick() >= TURN_MAX_ITEM) {
					agentBomberman.setIsSick(false);
					agentBomberman.setNbTurnMalusSick(0);
				} else {
					agentBomberman.setNbTurnMalusSick(agentBomberman.getNbTurnMalusSick() + 1);
				}
			}

			agentBomberman.executer(this);
		}

		//On supprime les items ramassés par les agents
		for(Item item : this._listItemsUtilise) {
			this._listItems.remove(item);
		}

		//Actions des PNJ survivant à l'explosion des bombes
		for(AgentPNJ agentPNJ : this._listAgentsPNJ) {
			agentPNJ.executer(this);
		}

		//Explosion des bombes
		for(Bombe bomb : this._listBombs) {
			bomb.explosion(this);
		}

		//On supprime les agents qui ont été detruit suite à l'explosion des bombes
		for(Agent agent : this._listAgentsDetruit) {
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
	boolean appartientMap(int x, int y) {
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

	int getProbabiliteObjet() {
		return PROBABILITE_OBJET;
	}

	void addListBombs(Bombe bomb) {
		this._listBombs.add(bomb);
	}

	void addListItems(Item item) {
		this._listItems.add(item);
	}

	void addListItemUtilise(Item item) {
		this._listItemsUtilise.add(item);
	}

	void addListBombeDetruite(Bombe bomb) {
		this._listBombesDetruite.add(bomb);
	}

	void addListAgentDetruit(Agent agent) {
		this._listAgentsDetruit.add(agent);
	}

	void removeAgentBomberman(AgentBomberman bomberman) {
	    this._listAgentsBomberman.remove(bomberman);
	}

	public ControllerBombermanGame getControllerBombGame() {
		return this._controllerBombGame;
	}

	public int getReward() {
		return this.reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	// Recupere l'agent PNJ qui possède les coordonnées passées en paramètre
	AgentPNJ getAgentPNJByCoord(int x, int y) {
		for(AgentPNJ agent : this._listAgentsPNJ) {
			if(agent.getX() == x && agent.getY() == y) {
				return agent;
			}
		}

		return null;
	}

	// Recupere l'agent Bomberman qui possède les coordonnées passées en paramètre
	public AgentBomberman getAgentBombermanByCoord(int x, int y) {
		for(AgentBomberman agent : this._listAgentsBomberman) {
			if(agent.getX() == x && agent.getY() == y) {
				return agent;
			}
		}

		return null;
	}

	// Recupere l'agent qui possède les coordonnées passées en paramètre
	Agent getAgentByCoord(int x, int y) {
		AgentBomberman agentBomberman = this.getAgentBombermanByCoord(x, y);
		AgentPNJ agentPNJ = this.getAgentPNJByCoord(x, y);

		if(agentBomberman != null) {
			return agentBomberman;
		} else {
			return agentPNJ;
		}
	}
	
	public ModeJeu getModeJeu() {
		return this.mode;
	}

	// Recupere l'agent Bomberman qui possède la bombe passée en paramètre
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

	public boolean gameContinue() {
		if(this._listAgentsBomberman.size() == 1) {
			if(this._listAgentsPNJ.size() > 0) {
				return true;
			}
			else return false;
		}
		return true;
	}

	public void gameOver() {
		System.out.println("Fin du jeu");

		if(this._listAgentsBomberman.size() <= 0) {
			System.out.println("Victoire des agents PNJ !");
			ViewGagnant viewGagnant = ViewGagnant.getInstance(this._controllerBombGame, "Victoire des agents PNJ !");
		}
		else if(this._listAgentsBomberman.size() == 1) {
			System.out.println("Victoire de l'agent '" + this._listAgentsBomberman.get(0).getColor() + "'");
			ViewGagnant viewGagnant = ViewGagnant.getInstance(this._controllerBombGame, "Victoire de l'agent '" + this._listAgentsBomberman.get(0).getColor() + "'");
		}
	}

}
