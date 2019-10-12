package Controller;

import Model.SimpleGame;
import View.ViewCommand;
import View.ViewSimpleGame;

public class ControllerSimpleGame implements InterfaceController {

	public SimpleGame _simpleGame;
	
	public ControllerSimpleGame(SimpleGame game) {
		this._simpleGame = game;
		@SuppressWarnings("unused")
		ViewCommand viewCommand = new ViewCommand(this, game);
		@SuppressWarnings("unused")
		ViewSimpleGame viewSimpleGame = new ViewSimpleGame(this, game);
		
	}
	
	public void start() {
		this._simpleGame.init();
	}

	public void step() {
		this._simpleGame.step();
	}

	public void run() {
		this._simpleGame.launch();		
	}

	public void stop() {
		this._simpleGame.stop();
	}

	public void setTime(long time) {
		this._simpleGame.setTime(time);
	}
	
	public long getTime() {
		return _simpleGame.getTime();
	}
	
	public int getInitTime() {
		return _simpleGame.INIT_TIME;
	}

}
