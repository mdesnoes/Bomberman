package Model;

public abstract class Agent {

	private static int _compteur = 0;
	private int _id;

	private int _pos_x;
	private int _pos_y;
	
	public Agent(int pos_x, int pos_y) {
		this._pos_x = pos_x;
		this._pos_y = pos_y;
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

	
	public abstract void moveAgent(AgentAction action);
}
