package View;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import Controller.InterfaceController;
import Model.BombermanGame;


public class ViewBombermanGame implements Observer {

	private Map _map;
	private PanelBomberman _panel;
	private InterfaceController _controllerGame;
	
	public ViewBombermanGame(InterfaceController controllerGame, BombermanGame bombermanGame) {
		this._controllerGame = controllerGame;
		bombermanGame.addObserver(this);
		this.createView();
	}
	
	private void createView() {
		JFrame jFrame = new JFrame();
		jFrame.setTitle("Bomberman Game");
		jFrame.setSize(new Dimension(1200,800));		

		try {
			this._map = new Map("layout/niveau2.lay");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this._panel = new PanelBomberman(this._map);
		
		
		jFrame.setContentPane(this._panel);
		jFrame.setVisible(true);
	}
	
	public Map getMap() {
		return this._map;
	}
	
	
	public void update(Observable o, Object arg) {
		this._panel.repaint();
	}

}
