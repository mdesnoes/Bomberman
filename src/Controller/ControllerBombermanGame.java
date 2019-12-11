package Controller;

import java.util.ArrayList;

import Model.*;
import View.InfoAgent;
import View.InfoBomb;
import View.InfoItem;
import View.Map;
import View.ViewBombermanGame;
import View.ViewCommand;
import View.ViewModeInteractif;

public class ControllerBombermanGame implements InterfaceController {

	private ViewCommand _viewCommand;
	private ViewBombermanGame _viewBombGame;
	private BombermanGame _bombGame;
	private ViewModeInteractif _viewModeInteractif;
	
	public ControllerBombermanGame(BombermanGame bombGame) {
		this._bombGame = bombGame;
		this._viewCommand = new ViewCommand(this, bombGame);
		this._viewBombGame = new ViewBombermanGame(this, bombGame);
	}
	
	public Map getMap() {
		return this._viewBombGame.getMap();
	}
	
	public void start() {
		this._bombGame.init();
	}

	public void step() {
		this._bombGame.step();
	}

	public void run() {
		this._bombGame.launch();
	}

	public void stop() {
		this._bombGame.stop();
	}

	public void setTime(long time) {
		this._bombGame.setTime(time);
	}

	public long getTime() {
		return _bombGame.getTime();
	}

	public int getInitTime() {
		return Game.INIT_TIME;
	}

	public String getLayout() {
		return this._viewCommand.getLayoutGame();
	}
	
	
	//Permet de renseigner les nouvelles coordonnées des agents à la liste d'InfoAgent et retourner cette liste
	public ArrayList<InfoAgent> getListInfoAgent() {
		ArrayList<InfoAgent> infoListAgent = new ArrayList<>();

		for(AgentBomberman agent : this._bombGame.getListAgentBomberman()) {
			infoListAgent.add(new InfoAgent(agent.getX(), agent.getY(), agent.getAction(), agent.getType(), agent.getColor(),agent.isInvincible(),agent.getIsSick()));
		}

		for(AgentPNJ agent : this._bombGame.getListAgentPNJ()) {
			infoListAgent.add(new InfoAgent(agent.getX(), agent.getY(), agent.getAction(), agent.getType(), agent.getColor(), false, false));
		}

		return infoListAgent;
	}

	public boolean[][] getListBreakableWall() {
		return this._bombGame.getListBreakableWall();
	}
	
	public ArrayList<InfoItem> getListInfoItems() {
		ArrayList<InfoItem> infoItemList = new ArrayList<>();

		for(Item item : this._bombGame.getListItem()) {
			infoItemList.add(new InfoItem(item.getX(), item.getY(), item.getType()));
		}

		return infoItemList;
	}
	
	public ArrayList<InfoBomb> getListInfoBombs() {
		ArrayList<InfoBomb> infoBombList = new ArrayList<>();

		for(Bombe bomb : this._bombGame.getListBomb()) {
			infoBombList.add(new InfoBomb(bomb.getX(), bomb.getY(), bomb.getRange(), bomb.getStateBomb()));
		}

		return infoBombList;
	}
	
	
	public void setViewModeInteractif() {
		this._viewModeInteractif = ViewModeInteractif.getInstance();
	}

	public ViewModeInteractif getViewModeInteractif() {
		return this._viewModeInteractif;
	}
	
	public ViewCommand getViewCommand() {
		return this._viewCommand;
	}

	public ViewBombermanGame getViewBombGame() {
		return this._viewBombGame;
	}
}
