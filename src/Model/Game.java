package Model;
import java.util.ArrayList;
import java.util.Observable;


public abstract class Game extends Observable implements Runnable {

	public static final int INIT_TIME = 1000;
	private int _turn;
	private int _maxturn;
	private boolean _isRunning;
	private Thread _thread;
	private long _time;
	
    private ArrayList<Agent> _agentList = new ArrayList<Agent>();
	
	public Game(int maxturn) {
		this._maxturn = maxturn;
		this._time = (long)INIT_TIME;
	}
	
	public void init() {
		this._turn = 0;
		this._isRunning = true;
		
		this.setChanged();
		this.notifyObservers();
		initialize_game();
	}
	
	public void step() {

		this.setChanged();
		this.notifyObservers();
		if(gameContinue() && this._turn <= this._maxturn) {
			this._turn++;
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
				Thread.sleep(this._time);
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
	
	
	
	public void addBombermanAgent(int pos_x, int pos_y, char type) {
    	AgentFactory agentFactory = new BombermanFactory();
    	this._agentList.add(agentFactory.createAgent(pos_x, pos_y, type));
    }
	
	
	public void addPNJAgent(int pos_x, int pos_y, char type) {
    	AgentFactory agentFactory = new PNJFactory();
    	this._agentList.add(agentFactory.createAgent(pos_x, pos_y, type));
    }

	
    public void executeOneTurn() {
        for (Agent agent: this._agentList) {
        	agent.executeAction();
        }
    }
	
	
	public void setTime(long time) {
		this._time = time;
	}
	
	
	public int getTurn( ) {
		return this._turn;
	}
	
	public int getMaxTurn() {
		return this._maxturn;
	}
	
	
	public long getTime() {
		return this._time;
	}
	
	public abstract void initialize_game();
	public abstract void takeTurn();
	public abstract boolean gameContinue();
	public abstract void gameOver();

}
