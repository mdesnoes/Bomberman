package Model;


public class PNJFactory implements AgentFactory {

	public Agent createAgent(int pos_x, int pos_y, char type) {
	
		switch(type) {
			case 'R': return new AgentRajion(pos_x, pos_y, type, ColorAgent.ROUGE);
			case 'V': return new AgentBird(pos_x, pos_y, type, ColorAgent.JAUNE);
			case 'E': return new AgentEnnemyBasique(pos_x, pos_y, type, ColorAgent.BLANC);
			default: return null;
		}
	}

}
