import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ViewCommand implements Observer {

	SimpleGame _game;
	
	public ViewCommand(SimpleGame game) {
		this._game = game;
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
		GridLayout layoutPrincipal = new GridLayout(2,1);
		panelPrincipal.setLayout(layoutPrincipal);
				
		JPanel panelButton = new JPanel();
		GridLayout layoutButton = new GridLayout(1,4);
		
		panelButton.setLayout(layoutButton);
		
		Icon icon_restart = new ImageIcon("icones/icon_restart.png");
		JButton buttonRestart = new JButton(icon_restart);
		Icon icon_run = new ImageIcon("icones/icon_run.png");
		JButton buttonRun = new JButton(icon_run);
		Icon icon_step = new ImageIcon("icones/icon_step.png");
		JButton buttonStep = new JButton(icon_step);
		Icon icon_pause = new ImageIcon("icones/icon_pause.png");
		JButton buttonPause = new JButton(icon_pause);

		panelButton.add(buttonRestart);
		panelButton.add(buttonRun);
		panelButton.add(buttonStep);
		panelButton.add(buttonPause);

		
		panelPrincipal.add(panelButton);
		
		JPanel panelSliderLabel = new JPanel();
		GridLayout layoutSliderLabel = new GridLayout(1,2);
		panelSliderLabel.setLayout(layoutSliderLabel);

		int graduation[] = new int[this._game.getMaxTurn()];
		
		
		JSlider sliderTurn = new JSlider(JSlider.HORIZONTAL,graduation[0],graduation[4],graduation[3]);
		sliderTurn.setPaintLabels(true);
		sliderTurn.setPaintTicks(true);
		sliderTurn.setName("Number of turns par second");
		
		
		
		JLabel labelTurn = new JLabel("Turn : " + this._game.getTurn());
		
		panelSliderLabel.add(sliderTurn);
		panelSliderLabel.add(labelTurn);
		
		panelPrincipal.add(panelSliderLabel);

		jFrame.setContentPane(panelPrincipal);
		jFrame.setVisible(true);
		
	}
	
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
