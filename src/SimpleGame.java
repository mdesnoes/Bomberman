import java.util.ArrayList;

public class SimpleGame extends Game implements Observable {

	private ArrayList<Observer> _observers;
	
	public SimpleGame(int maxturn) {
		super(maxturn);
		this._observers = new ArrayList<Observer>();
	}

	public void initialize_game() {
		System.out.println("Le jeu est initialisé");
		
		notifyObserver();
	}

	public void takeTurn() {
		System.out.println("Tour " + this.getTurn() + " du jeu en cours");
		
		notifyObserver();
	}

	public boolean gameContinue() {
		System.out.println("Le jeu continue");
		return true;
	}

	public void gameOver() {
		System.out.println("Fin du jeu");
		
		notifyObserver();
	}

	
	
	
	@Override
	public void registerObserver(Observer observer) {
		this._observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		this._observers.remove(observer);
	}

	@Override
	public void notifyObserver() {
		for(Observer obs : this._observers) {
			obs.update();
		}
	}
}
