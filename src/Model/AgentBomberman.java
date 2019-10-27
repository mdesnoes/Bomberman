package Model;

public class AgentBomberman extends Agent {

	private ColorAgent _color;
	
	public AgentBomberman(int pos_x, int pos_y, ColorAgent color) {
		super(pos_x,pos_y);
		this._color = color;
	}

	public void moveAgent(AgentAction action) {
		
		switch(action) {
			case MOVE_UP: this.setY(this.getY() - 1); break;
			case MOVE_DOWN: this.setY(this.getY() + 1); break;
			case MOVE_LEFT: this.setX(this.getX() - 1); break;
			case MOVE_RIGHT: this.setX(this.getX() + 1); break;
			case STOP: break;
			case PUT_BOMB: break;
		}
		
	}

}
