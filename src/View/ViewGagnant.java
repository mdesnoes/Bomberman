package View;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Controller.ControllerBombermanGame;


public class ViewGagnant extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private static ViewGagnant uniqueInstance = null;


	private ViewGagnant(ControllerBombermanGame contBombGame, String stringVictoire) {
		setTitle("Fin du jeu");
		setSize(new Dimension(400,150));
		Dimension windowSize = getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2;
		int dy = centerPoint.y - windowSize.height / 2;
		setLocation(dx,dy);
	
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new GridLayout(2,1));
		
		JLabel labelJoueur1 = new JLabel(stringVictoire, JLabel.CENTER);
		//labelJoueur1.setForeground(colorGagnant);
		panelPrincipal.add(labelJoueur1);
		
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new GridLayout(1,2));
		
		JButton buttonRecommencer = new JButton("Recommencer");
		JButton buttonFermer = new JButton("Fermer le jeu");
		
		panelButton.add(buttonRecommencer);
		panelButton.add(buttonFermer);

		buttonRecommencer.addActionListener(evenement -> {
			contBombGame.start();
			setVisible(false);
		});
		
		buttonFermer.addActionListener(evenement -> {
			setVisible(false);
			contBombGame.getViewBombGame().setVisible(false);
			contBombGame.getViewCommand().setVisible(false);
			if(contBombGame.getViewModeInteractif() != null) {
				contBombGame.getViewModeInteractif().setVisible(false);
			}
		});
		panelPrincipal.add(panelButton);
		
		setContentPane(panelPrincipal);
		setVisible(true);
	}
	
	public static ViewGagnant getInstance(ControllerBombermanGame contBombGame, String stringVictoire) {
		if(uniqueInstance == null) {
			uniqueInstance = new ViewGagnant(contBombGame, stringVictoire);
		}
		return uniqueInstance;
	}

}
