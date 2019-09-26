import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import javax.swing.JFrame;


public class ViewSimpleGame implements Observer {

	SimpleGame _game;
	
	public ViewSimpleGame(SimpleGame game) {
		this._game = game;
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
		jFrame.setVisible(true);

	}
	
	public void update() {

	}

}
