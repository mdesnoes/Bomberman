package Model;

import java.util.ArrayList;

public abstract class Agent {

	private static int _compteur = 0;
	private int _id;

	private int _pos_x;
	private int _pos_y;
	private ColorAgent _color;
	private AgentAction _action;
	private char _type;
	
	private ArrayList<Bombe> _listBombes = new ArrayList<Bombe>();
	private int _nbBombe = 1;
	
	private boolean _isInvincible = false;
	private boolean _isSick = false;
	
	private int _nbTurnBonusInvincible = 0;
	private int _nbTurnMalusSick = 0;
	
	private int _rangeBomb = 2;
	
	public Agent(int pos_x, int pos_y, char type, ColorAgent color) {
		this._pos_x = pos_x;
		this._pos_y = pos_y;
		this._type = type;
		this._color = color;
		this._id = Agent._compteur;
		Agent._compteur++;
	}
	
	public void setX(int x) {
		this._pos_x = x;
	}
	
	public void setY(int y) {
		this._pos_y = y;
	}
	
	public int getX() {
		return this._pos_x;
	}
	
	public int getY() {
		return this._pos_y;
	}

	public ColorAgent getColor() {
		return this._color;
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
	
	public char getType() {
		return this._type;
	}
	
	public AgentAction getAction() {
		return this._action;
	}
	
	public void setAction(AgentAction action) {
		this._action = action;
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
	
	public abstract void moveAgent(AgentAction action);
}
