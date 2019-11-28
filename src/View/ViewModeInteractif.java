package View;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Model.AgentAction;

public class ViewModeInteractif extends JFrame implements KeyListener {
	
	private static final long serialVersionUID = 1L;
	private static ViewModeInteractif uniqueInstance = null;
	AgentAction action = null;
	
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
		
		int codeKey = key.getKeyCode();
         
        switch (codeKey)
        {
            case KeyEvent.VK_UP:
                this.action = AgentAction.MOVE_UP;
                break;
            case KeyEvent.VK_LEFT:
            	this.action = AgentAction.MOVE_LEFT;
                break;
            case KeyEvent.VK_RIGHT:
            	this.action = AgentAction.MOVE_RIGHT;
                break;
            case KeyEvent.VK_DOWN:
            	this.action = AgentAction.MOVE_DOWN;
                break;
            case KeyEvent.VK_NUMPAD0:
            	this.action = AgentAction.PUT_BOMB;
            	break;
            case KeyEvent.VK_B:
            	this.action = AgentAction.STOP;
            	break;
        }
		
	}

	@Override
	public void keyReleased(KeyEvent key) {
		
	}

	@Override
	public void keyTyped(KeyEvent key) {
		
	}
	
	public AgentAction getAction() {
		return this.action;
	}
}
