package Controller;

import Model.BombermanGame;
import View.ViewBombermanGame;
import View.ViewCommand;

public class ControllerBombermanGame implements InterfaceController {

	private BombermanGame _bombGame;
	
	public ControllerBombermanGame(BombermanGame bombGame) {
		this._bombGame = bombGame;
		ViewCommand viewCommand = new ViewCommand(this, bombGame);
		ViewBombermanGame viewBombGame = new ViewBombermanGame(this, bombGame);
	}
	
	@Override
	public void start() {
		this._bombGame.init();
	}

	@Override
	public void step() {
		this._bombGame.step();
		
	}

	@Override
	public void run() {
		this._bombGame.launch();		
		
	}

	@Override
	public void stop() {
		this._bombGame.stop();
		
	}

	@Override
	public void setTime(long time) {
		this._bombGame.setTime(time);
		
	}

	@Override
	public long getTime() {
		return _bombGame.getTime();
	}

	@Override
	public int getInitTime() {
		return _bombGame.INIT_TIME;
	}

}
