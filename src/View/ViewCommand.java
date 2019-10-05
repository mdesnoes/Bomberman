package View;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Controller.InterfaceController;
import Model.Game;
import java.util.Observable;
import java.util.Observer;

public class ViewCommand implements Observer {

	private final static int SPEEDMIN = 1;		// Vitesse minimum des tours
	private final static int SPEEDMAX = 10;		// Vitesse maximum des tours
	private JLabel _labelTurn;					// Le label qui affiche le tour courant
	private JSlider _slider;					// Le slider qui affiche la vitesse des tours en seconde
	private InterfaceController _controllerGame;
	
	
	public ViewCommand(InterfaceController controllerGame, Game game) {
		this._controllerGame = controllerGame;
		game.addObserver(this);
		this.createView();
	}
	
	public void createView() {
		JFrame jFrame = new JFrame();
		jFrame.setTitle("Commande");
		jFrame.setSize(new Dimension(1400,400));
		Dimension windowSize = jFrame.getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();
		int dx = centerPoint.x - windowSize.width / 2;
		int dy = centerPoint.y - windowSize.height / 2 - 350;
		jFrame.setLocation(dx,dy);
				
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new GridLayout(2,1));
				
		JPanel panelButton = new JPanel();		
		panelButton.setLayout(new GridLayout(1,4));
		
		Icon icon_restart = new ImageIcon("icones/icon_restart.png");
		JButton buttonRestart = new JButton(icon_restart);
		//Permet d'appeler le controleur afin d'initialiser le jeu
		buttonRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				_controllerGame.start();
			}
		});
		
		Icon icon_run = new ImageIcon("icones/icon_run.png");
		JButton buttonRun = new JButton(icon_run);
		//Permet d'appeler le controleur afin de demarrer le jeu
		buttonRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				_controllerGame.run();
			}
		});
		Icon icon_step = new ImageIcon("icones/icon_step.png");
		JButton buttonStep = new JButton(icon_step);
		//Permet d'appeler le controleur afin de passer au tour suivant
		buttonStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				_controllerGame.step();
			}
		});
		//Permet d'appeler le controleur afin de faire une pause
		Icon icon_stop = new ImageIcon("icones/icon_pause.png");
		JButton buttonStop = new JButton(icon_stop);
		buttonStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evenement) {
				_controllerGame.stop();
			}
		});

		panelButton.add(buttonRestart);
		panelButton.add(buttonRun);
		panelButton.add(buttonStep);
		panelButton.add(buttonStop);

				
		JPanel panelSliderLabel = new JPanel();
		panelSliderLabel.setLayout(new GridLayout(1,2));

		
		JPanel panelSlider = new JPanel();
		panelSlider.setLayout(new GridLayout(2,1));
		
		JLabel labelSlider = new JLabel("Number of turns par second");
		labelSlider.setHorizontalAlignment(JLabel.CENTER);
		panelSlider.add(labelSlider);
		
		this._slider = new JSlider(JSlider.HORIZONTAL,SPEEDMIN,SPEEDMAX,SPEEDMIN);
		this._slider.setPaintTicks(true);
		this._slider.setPaintLabels(true);
		this._slider.setMinorTickSpacing(SPEEDMIN);
		this._slider.setMajorTickSpacing(SPEEDMIN);
		this._slider.setValue((int)this._controllerGame.getTime()/1000);
		//Permet d'appeler le constructeur afin de modifier le temps des tours
		this._slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				_controllerGame.setTime(_controllerGame.getInitTime()/_slider.getValue());
			}
		});
		
		panelSlider.add(this._slider);
				
		
		this._labelTurn = new JLabel("Turn : 0");
		this._labelTurn.setHorizontalAlignment(JLabel.CENTER);
		this._labelTurn.setVerticalAlignment(JLabel.CENTER);
		
		panelSliderLabel.add(panelSlider);
		panelSliderLabel.add(this._labelTurn);
		
		
		panelPrincipal.add(panelButton);
		panelPrincipal.add(panelSliderLabel);

		jFrame.setContentPane(panelPrincipal);
		jFrame.setVisible(true);
	}

	public void update(Observable obs, Object arg) {
		Game game = (Game)obs;
		this._labelTurn.setText("Turn : " + game.getTurn());
		
		System.out.println("Time : " + game.getTime());
	}

}
