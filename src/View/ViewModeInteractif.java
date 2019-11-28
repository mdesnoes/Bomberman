package View;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ViewModeInteractif extends JFrame implements KeyListener {
	
	private static final long serialVersionUID = 1L;
	private static ViewModeInteractif uniqueInstance = null;
	int codeKeyPressed;
	
	private ViewModeInteractif() {
		setTitle("Action de l'agent Bomberman");
		setSize(new Dimension(300,300));
		Dimension windowSize = getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2 + 1000;
		int dy = centerPoint.y - windowSize.height / 2 - 1000;
		setLocation(dx,dy);
	
		JPanel panelCommande = new JPanel();
		panelCommande.setLayout(new GridLayout(3,3));
		
		
		
		JButton buttonUp = new JButton("Up");
		JButton buttonDown = new JButton("Down");
		JButton buttonLeft = new JButton("Left");
		JButton buttonRight = new JButton("Right");
		JButton buttonPutBomb = new JButton("Put Bomb");
		JButton buttonStop = new JButton("Stop");
		
		panelCommande.add(buttonPutBomb);
		panelCommande.add(buttonUp);
		panelCommande.add(new JLabel(""));
		panelCommande.add(buttonLeft);
		panelCommande.add(buttonStop);
		panelCommande.add(buttonRight);
		panelCommande.add(new JLabel(""));
		panelCommande.add(buttonDown);
		panelCommande.add(new JLabel(""));
	
		
		setContentPane(panelCommande);
		setVisible(true);
	}
	
	public static ViewModeInteractif getInstance() {
		if(uniqueInstance == null) {
			uniqueInstance = new ViewModeInteractif();
		}
		return uniqueInstance;
	}

	@Override
	public void keyPressed(KeyEvent key) {
		this.codeKeyPressed = key.getKeyCode();
	}

	@Override
	public void keyReleased(KeyEvent key) {	
	}

	@Override
	public void keyTyped(KeyEvent key) {
	}
	
	public int getKeyPressed() {
		return this.codeKeyPressed;
	}
}
