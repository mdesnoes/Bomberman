package Model;

public abstract class FactoryProvider {
	
	public static AgentFactory getFactory(char typeAgent) {
		AgentFactory agentFactory = null;
		switch(typeAgent) {
		case 'B':
			agentFactory = new BombermanFactory();
			break;
		default:
			agentFactory = new PNJFactory();
			break;
		}
		return agentFactory;
	}
}
