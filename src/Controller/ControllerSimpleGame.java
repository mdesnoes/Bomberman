package Controller;

import Model.Game;
import View.ViewCommand;
import View.ViewSimpleGame;

public class ControllerSimpleGame implements InterfaceController {

	public Game _game;
	
	public ControllerSimpleGame(Game game) {
		this._game = game;
		@SuppressWarnings("unused")
		ViewCommand viewCommand = new ViewCommand(this, game);
		@SuppressWarnings("unused")
		ViewSimpleGame viewSimpleGame = new ViewSimpleGame(this, game);
		
	}
	
	public void start() {
		this._game.init();
	}

	public void step() {
		this._game.step();
	}

	public void run() {
		this._game.launch();		
	}

	public void stop() {
		this._game.stop();
	}

	public void setTime(long time) {
		this._game.setTime(time);
	}
}
