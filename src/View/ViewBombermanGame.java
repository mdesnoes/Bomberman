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

		Map m = null;
		try {
			m = new Map("layout/alone.lay");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PanelBomberman p = new PanelBomberman(m);
		
		//p.repaint();
		
		
		
		jFrame.setContentPane(p);
		jFrame.setVisible(true);
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		
	}

}
