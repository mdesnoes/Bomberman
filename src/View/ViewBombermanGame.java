package View;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import Controller.ControllerBombermanGame;
import Model.AgentAction;
import Model.BombermanGame;


public class ViewBombermanGame implements Observer {

	private JFrame _jFrame;
	private Map _map;
	private PanelBomberman _panel;
	private ControllerBombermanGame _controllerGame;

	
	public ViewBombermanGame(ControllerBombermanGame controllerGame, BombermanGame bombermanGame) {
		this._controllerGame = controllerGame;
		bombermanGame.addObserver(this);
		this.createView();
	}
	
	private void createView() {
		_jFrame = new JFrame();
		_jFrame.setTitle("Bomberman Game");
		_jFrame.setSize(new Dimension(1100,700));
		Dimension windowSize = _jFrame.getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2 - 600;
		int dy = centerPoint.y - windowSize.height / 2 - 600;
		_jFrame.setLocation(dx,dy);

		try {
			this._map = new Map("layout/" + this._controllerGame.getLayout());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this._panel = new PanelBomberman(this._map);
		
		
		_jFrame.setContentPane(this._panel);
		_jFrame.setVisible(true);
	}
	
	public Map getMap() {
		return this._map;
	}
	
	public void setMap(Map map) {
		this._map = map;
	}
	
	public void update(Observable obs, Object arg) {
		
		this._panel.setInfoGame(this._controllerGame.getListBreakableWall(), this._controllerGame.getListInfoAgent(),
			this._controllerGame.getListInfoItems(), this._controllerGame.getListInfoBombs());
		
		this._panel.repaint();
	}

}
