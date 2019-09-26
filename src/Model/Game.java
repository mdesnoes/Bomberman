package Model;

public abstract class Game implements Runnable {

	private int _turn;
	private int _maxturn;
	private boolean _isRunning;
	private Thread _thread;
	private final static double _time = 1000;
	
	public Game(int maxturn) {
		this._maxturn = maxturn;
	}
	
	public void init() {
		this._turn = 0;
		this._isRunning = true;
		
		initialize_game();
	}
	
	public void step() {
		this._turn++;

		if(gameContinue() && this._turn <= this._maxturn) {
			takeTurn();
		}
		else {
			this._isRunning = false;
			gameOver();
		}
	}
	
	public void run() {
		while(this._isRunning) {
			step();
			
			try {
				Thread.sleep((long)Game._time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		this._isRunning = false;
	}
	
	public void launch() {
		this._isRunning = true;
		this._thread = new Thread(this);
		this._thread.start();
	}
	
	public int getTurn( ) {
		return this._turn;
	}
	
	public int getMaxTurn() {
		return this._maxturn;
	}
	
	public abstract void initialize_game();
	public abstract void takeTurn();
	public abstract boolean gameContinue();
	public abstract void gameOver();

}
