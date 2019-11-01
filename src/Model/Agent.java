package Model;

public abstract class Agent {

	private static int _compteur = 0;
	private int _id;

	private int _pos_x;
	private int _pos_y;
	private ColorAgent _color;
	private AgentAction _action;
	private char _type;
	
	private Bombe _bombe;
	
	private boolean _isInvincible = false;
	private boolean _isSick = false;
	
	private int _nbTurnBonusInvincible = 0;
	private int _nbTurnMalusSick = 0;
	
	public Agent(int pos_x, int pos_y, char type, ColorAgent color) {
		this._pos_x = pos_x;
		this._pos_y = pos_y;
		this._type = type;
		this._color = color;
		this._id = Agent._compteur;
		Agent._compteur++;
		this._bombe = null;
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

	public void setBombe(Bombe bombe) {
		this._bombe = bombe;
	}
	
	public Bombe getBombe() {
		return this._bombe;
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
	
	
	public abstract void moveAgent(AgentAction action);
}
