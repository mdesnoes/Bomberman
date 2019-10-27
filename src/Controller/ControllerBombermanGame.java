package Controller;


import java.util.ArrayList;

import Model.Bombe;
import Model.BombermanGame;
import Model.InfoAgent;
import Model.InfoBomb;
import Model.InfoItem;
import Model.Item;
import View.Map;
import View.ViewBombermanGame;
import View.ViewCommand;

public class ControllerBombermanGame implements InterfaceController {

	private ViewCommand _viewCommand;
	private ViewBombermanGame _viewBombGame;
	private BombermanGame _bombGame;
	
	public ControllerBombermanGame(BombermanGame bombGame) {
		this._bombGame = bombGame;
		this._viewCommand = new ViewCommand(this, bombGame);
		this._viewBombGame = new ViewBombermanGame(this, bombGame);
	}
	
	public Map getMap() {
		return this._viewBombGame.getMap();
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

	public void setTime(long time) {
		this._bombGame.setTime(time);
		
	}

	public long getTime() {
		return _bombGame.getTime();
	}

	public int getInitTime() {
		return _bombGame.INIT_TIME;
	}

	public String getLayout() {
		return this._viewCommand.getLayout();
	}
	
	
	//Permet de renseigner les nouvelles coordonnées des agents à la liste d'InfoAgent et retourner cette liste
	public ArrayList<InfoAgent> getListInfoAgent() {
		ArrayList<InfoAgent> infoAgentList = this._viewBombGame.getMap().getStart_agents();
		
		if(this._bombGame.getAgentList().size() == infoAgentList.size()) {
			for(int i=0; i<infoAgentList.size(); ++i) {
				infoAgentList.get(i).setX(this._bombGame.getAgentList().get(i).getX());
				infoAgentList.get(i).setY(this._bombGame.getAgentList().get(i).getY());
			}
		}
		
		return infoAgentList;
	}

	public boolean[][] getListBreakableWall() {
		return this._bombGame.getListBreakableWall();
	}
	
	public ArrayList<InfoItem> getListInfoItems() {
		ArrayList<InfoItem> infoItemList = new ArrayList<InfoItem>();
		for(Item item : this._bombGame.getListItem()) {
			infoItemList.add(new InfoItem(item.getX(), item.getY(), item.getType()));
		}
		return infoItemList;
	}
	
	public ArrayList<InfoBomb> getListInfoBombs() {
		ArrayList<InfoBomb> infoBombList = new ArrayList<InfoBomb>();
		for(Bombe bomb : this._bombGame.getListBomb()) {
			infoBombList.add(new InfoBomb(bomb.getX(), bomb.getY(), bomb.getRange(), bomb.getStateBomb()));
		}
		return infoBombList;
	}
	
}
