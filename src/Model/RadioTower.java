package Model;

public class RadioTower {

	private int x;
	private int y;
	private static final int MAX_RAIJON = 5;
	private int nbRaijon; 
	
	public RadioTower(int x, int y) {
		this.x = x;
		this.y = y;
		this.nbRaijon = this.MAX_RAIJON;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
}
