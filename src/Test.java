
public class Test {

	public static void main(String[] args) {

		SimpleGame game1 = new SimpleGame(5);
		ViewSimpleGame viewGame1 = new ViewSimpleGame(game1);
		//game1.init();
		//game1.run();
		
		//viewGame1.createView();
		
		ViewCommand viewCommand = new ViewCommand(game1);
		viewCommand.createView();
	}

}
