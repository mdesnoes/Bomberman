package View;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Controller.InterfaceController;
import Model.Game;
import Model.Observable;
import Model.Observer;
import Model.SimpleGame;


public class ViewSimpleGame implements Observer {
	
	private JLabel _labelTurn;
	private InterfaceController _controllerGame;
	
	public ViewSimpleGame(InterfaceController controllerGame, Game game) {
		this._controllerGame = controllerGame;
		game.registerObserver(this);
		this.createView();
	}
	
	public void createView() {
		
		JFrame jFrame = new JFrame();
		jFrame.setTitle("Game");
		jFrame.setSize(new Dimension(700,700));
		Dimension windowSize = jFrame.getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2;
		int dy = centerPoint.y - windowSize.height / 2 - 350;
		jFrame.setLocation(dx,dy);

		this._labelTurn = new JLabel("Current turn 0");
		this._labelTurn.setHorizontalAlignment(JLabel.CENTER);
		this._labelTurn.setVerticalAlignment(JLabel.NORTH);
		
		jFrame.setContentPane(this._labelTurn);
		jFrame.setVisible(true);

 	}
	
	
	public void update(Model.Observable obs) {
		Game game = (Game)obs;
		this._labelTurn.setText("Current turn " + game.getTurn());
	}

}
