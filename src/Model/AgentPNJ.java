package Model;

public abstract class AgentPNJ extends Agent {

	public AgentPNJ(int pos_x, int pos_y) {
		super(pos_x, pos_y);
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
