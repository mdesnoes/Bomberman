import Controller.ControllerSimpleGame;
import Model.Game;
import Model.SimpleGame;
import View.ViewCommand;
import View.ViewSimpleGame;

public class Test {

	public static void main(String[] args) {

		Game game1 = new SimpleGame(30);
		ControllerSimpleGame controllerGame = new ControllerSimpleGame(game1);
	}

}
