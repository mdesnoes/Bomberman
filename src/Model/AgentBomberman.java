package Model;

import java.util.ArrayList;

import Strategy.EsquiveStrategy;
import Strategy.PutBombStrategy;
import Strategy.RandomStrategy;

public class AgentBomberman extends Agent {

	private ArrayList<Bombe> _listBombes = new ArrayList<Bombe>();
	private int _nbBombe = 1;
	
	private boolean _isInvincible = false;
	private boolean _isSick = false;
	
	private int _nbTurnBonusInvincible = 0;
	private int _nbTurnMalusSick = 0;
	
	private int _rangeBomb = 2;
	
	
	public AgentBomberman(int pos_x, int pos_y, char type, ColorAgent color) {
		super(pos_x,pos_y, type, color);
		
		this.setStrategy(new PutBombStrategy());
	}
	
	public void executer(BombermanGame bombermanGame) {
		
		AgentAction action = this.getStrategy().chooseAction(bombermanGame, this);
		
		if(action == AgentAction.PUT_BOMB) {
			if(this.canPutBomb()) {
				Bombe bombe = new Bombe(this.getX(), this.getY(), this.getRangeBomb(), StateBomb.Step1);
				bombermanGame.addListBombs(bombe);
				this.addBombe(bombe);
			}
		}
		else if(action == AgentAction.MOVE_UP || action == AgentAction.MOVE_DOWN 
				|| action == AgentAction.MOVE_LEFT || action == AgentAction.MOVE_RIGHT) {
			if(this.isLegalMove(bombermanGame, action)) {
				this.moveAgent(action);
				
				for(Item item : bombermanGame.getListItem()) {
					if(this.getX() == item.getX() && this.getY() == item.getY()) {
						bombermanGame.takeItem(this, item);
						bombermanGame.addListItemUtilise(item);
					}
				}
			}
		}
		
		//this.setAction(action);
	}
	
	
	@Override
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
    	if(!bombGame.appartientMap(newX,  newY)) {
    		return false;
    	}
    	
    	//On verifie s'il y a un mur, un mur cassable ou une bombe sur la nouvelle case
    	if(bombGame.getControllerBombGame().getMap().get_walls()[newX][newY] || bombGame.getListBreakableWall()[newX][newY] || bombGame.isBomb(newX, newY)) {
    		return false;
    	}
    	
    	 //Un agent bomberman ne peut pas se deplacer sur un autre agent
		if(bombGame.getAgentByCoord(newX, newY) != null) {
			return false;
		}
    	
    	return true;
	}


	public void moveAgent(AgentAction action) {
		
		switch(action) {
			case MOVE_UP: this.setY(this.getY() - 1); break;
			case MOVE_DOWN: this.setY(this.getY() + 1); break;
			case MOVE_LEFT: this.setX(this.getX() - 1); break;
			case MOVE_RIGHT: this.setX(this.getX() + 1); break;
			default: break;
		}
		
	}
	
	public void addBombe(Bombe bomb) {
		this._listBombes.add(bomb);
	}
	
	public void removeBombe(Bombe bomb) {
		this._listBombes.remove(bomb);
	}
	
	public boolean canPutBomb() {
		if(this._isSick) {
			return false;
		}
		return this._listBombes.size() < this._nbBombe;
	}
	
	
	public void setListBombe(ArrayList<Bombe> bombes) {
		this._listBombes = bombes;
	}
	
	public ArrayList<Bombe> getListBombe() {
		return this._listBombes;
	}
	
	public int getNbTurnBonusInvincible() {
		return this._nbTurnBonusInvincible;
	}
	
	public void setNbTurnBonusInvincible(int i) {
		this._nbTurnBonusInvincible = i;
	}
	
	public int getNbTurnMalusSick() {
		return this._nbTurnMalusSick;
	}
	
	public void setNbTurnMalusSick(int i) {
		this._nbTurnMalusSick = i;
	}
	
	public int getNbBombe() {
		return this._nbBombe;

	}
	public void setNbBombe(int nb) {
		if(nb >= 1) {
			this._nbBombe = nb;
		}
	}
	
	public int getRangeBomb() {
		return this._rangeBomb;
	}
	
	public void setRangeBomb(int range) {
		if(range >= 1) {
			this._rangeBomb = range;
		}
	}
	
	public boolean getIsInvincible() {
		return this._isInvincible;
	}
	
	public void setIsInvincible(boolean b) {
		this._isInvincible = b;
	}
	
	public boolean getIsSick() {
		return this._isSick;
	}
	
	public void setIsSick(boolean b) {
		this._isSick = b;
	}
}
