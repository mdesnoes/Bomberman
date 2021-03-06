package View;

import Model.AgentAction;
import Model.ColorAgent;

public class InfoAgent {
	
	private int x;
	private int y;
	private AgentAction agentAction;
	private ColorAgent color;
	private char type;
	private boolean isInvincible;
	private boolean isSick;
	
	public InfoAgent(int x, int y, AgentAction agentAction, char type, ColorAgent color, boolean isInvincible, boolean isSick) {
		this.x=x;
		this.y=y;
		this.agentAction = agentAction;
		this.color = color;
		this.type = type;
		this.isInvincible = isInvincible;
		this.isSick = isSick;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	ColorAgent getColor() {
		return color;
	}

	public void setColor(ColorAgent color) {
		this.color = color;
	}
	
	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	boolean isInvincible() {
		return isInvincible;
	}

	public void setInvincible(boolean isInvincible) {
		this.isInvincible = isInvincible;
	}

	boolean isSick() {
		return isSick;
	}

	public void setSick(boolean isSick) {
		this.isSick = isSick;
	}

	AgentAction getAgentAction() {
		return agentAction;
	}

	public void setAgentAction(AgentAction agentAction) {
		this.agentAction = agentAction;
	}

}
	