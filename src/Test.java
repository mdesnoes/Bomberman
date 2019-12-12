import java.util.ArrayList;

import Model.BombermanGame;
import Model.ModeJeu;
import Strategy.PutBombStrategy;
import Strategy.RandomStrategy;
import Strategy.Strategy;
import View.ViewBombermanGame;

public class Test {

	public static void main(String[] args) {
	
		BombermanGame bombGame = new BombermanGame(ModeJeu.NORMAL, new PutBombStrategy(),1000);
		
		System.out.println("Début de la simulation");
		//System.out.println("Reward Average : " + getAverageReward(100, 100, new RandomStrategy()));
		System.out.println("Fin de la simulation");
		
		
		//visualize(100, new PutBombStrategy(), 100);
	}

	
	public static float getAverageReward(int nbSimu, int nbTour, Strategy strategy) {
		ArrayList<BombermanGame> listBombermanGame = new ArrayList<BombermanGame>();
		
		int reward = 0;
		for(int i=0; i<nbSimu; ++i) {
			BombermanGame bombGame = new BombermanGame(ModeJeu.PERCEPTRON, strategy, nbTour);
						
			bombGame.init();
			bombGame.setTime(0);

			bombGame.launch();
			
			
			listBombermanGame.add(bombGame);
		}
		
		for(BombermanGame bombermanGame : listBombermanGame) {
            try {
    			reward += bombermanGame.getReward();
                bombermanGame.getThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }		
		
		return reward / nbSimu;
	}
	
	public static void visualize (int nbTour, Strategy strategy, int pause) {
		BombermanGame bombGame = new BombermanGame(ModeJeu.PERCEPTRON, strategy, nbTour);
		bombGame.setTime(pause);
		bombGame.init();
		
		ViewBombermanGame v = new ViewBombermanGame(bombGame.getControllerBombGame(), bombGame);

		bombGame.getControllerBombGame().setViewBombermanGame(v);
		
		bombGame.launch();
	}

}
