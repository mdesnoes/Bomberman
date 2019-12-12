import Model.BombermanGame;
import Model.ModeJeu;

public class Test {

	public static void main(String[] args) {
	
		BombermanGame bombGame = new BombermanGame(ModeJeu.NORMAL,1000);
		
		System.out.println("Début de la simulation");
		//System.out.println("Reward Average : " + getAverageReward(10, 100));
		System.out.println("Fin de la simulation");
	}

	
	public static float getAverageReward(int nbSimu, int nbTour) {
		
		int reward = 0;
		for(int i=0; i<nbSimu; ++i) {
			BombermanGame bombGame = new BombermanGame(ModeJeu.PERCEPTRON, nbTour);
						
			bombGame.init();
			
			bombGame.setTime(0);

			bombGame.launch();
			
			reward += bombGame.getReward();
		}
		
		return reward / nbSimu;
	}
}
