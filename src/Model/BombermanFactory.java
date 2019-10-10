package Model;

public class BombermanFactory implements AgentFactory {

	private ColorAgent[] _tabColor = { ColorAgent.BLEU, ColorAgent.ROUGE, ColorAgent.VERT, ColorAgent.JAUNE, ColorAgent.BLANC };
	private static int _cptColor = 0;
	
	public Agent createAgent(int pos_x, int pos_y, char type) {
		return new AgentBomberman(pos_x, pos_y, this._tabColor[this._cptColor++]);
	}

}
