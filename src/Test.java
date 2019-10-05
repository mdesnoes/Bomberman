import Controller.ControllerBombermanGame;
import Controller.ControllerSimpleGame;
import Controller.InterfaceController;
import Model.BombermanGame;
import Model.SimpleGame;


public class Test {

	public static void main(String[] args) {
		/*
		SimpleGame game1 = new SimpleGame(100);
		InterfaceController controllerGame = new ControllerSimpleGame(game1);
		*/
		
		BombermanGame bombGame = new BombermanGame(100);
		InterfaceController controllerGame = new ControllerBombermanGame(bombGame);
	}

}
