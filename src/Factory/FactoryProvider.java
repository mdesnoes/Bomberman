package Factory;

import org.jetbrains.annotations.NotNull;

public abstract class FactoryProvider {
	
	@NotNull
	public static AgentFactory getFactory(char typeAgent) {
		AgentFactory agentFactory;

		if (typeAgent == 'B') {
			agentFactory = new BombermanFactory();
		} else {
			agentFactory = new PNJFactory();
		}

		return agentFactory;
	}

}
