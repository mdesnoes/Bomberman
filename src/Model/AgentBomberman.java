package Model;

public class AgentBomberman extends Agent {

	
	public AgentBomberman(int pos_x, int pos_y, char type, ColorAgent color) {
		super(pos_x,pos_y, type, color);
	}


	public void moveAgent(AgentAction action) {
		
		switch(action) {
			case MOVE_UP: this.setY(this.getY() - 1); break;
			case MOVE_DOWN: this.setY(this.getY() + 1); break;
			case MOVE_LEFT: this.setX(this.getX() - 1); break;
			case MOVE_RIGHT: this.setX(this.getX() + 1); break;
		}
		
	}

}
