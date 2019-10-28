package Model;

public abstract class Agent {

	private static int _compteur = 0;
	private int _id;

	private int _pos_x;
	private int _pos_y;
	private ColorAgent _color;
	private AgentAction _action;
	private char _type;
	
	
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
	
	public char getType() {
		return this._type;
	}
	
	public AgentAction getAction() {
		return this._action;
	}
	
	public void setAction(AgentAction action) {
		this._action = action;
	}

	
	public abstract void moveAgent(AgentAction action);
}
