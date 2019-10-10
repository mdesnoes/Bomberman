package Model;

public class BombermanGame extends Game {

	public BombermanGame(int maxturn) {
		super(maxturn);
	}

	@Override
	public void initialize_game() {
		System.out.println("Le jeu est initialisé");		
	}

	@Override
	public void takeTurn() {
		System.out.println("Tour " + this.getTurn() + " du jeu en cours");		
	}

	@Override
	public boolean gameContinue() {
		System.out.println("Le jeu continue");
		return true;
	}

	@Override
	public void gameOver() {
		System.out.println("Fin du jeu");		
	}

}
