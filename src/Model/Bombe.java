package Model;

public class Bombe {
	
	private int _pos_x;
	private int _pos_y;
	private int _range;
	StateBomb _stateBomb;
	
	public Bombe(int x, int y, int range, StateBomb stateBomb) {
		this._pos_x = x;
		this._pos_y = y;
		this._range = range;
		this._stateBomb = stateBomb;
	}
	
	public int getX() {
		return this._pos_x;
	}

	public int getY() {
		return this._pos_y;
	}

	public int getRange() {
		return this._range;
	}
	
	public void setRange(int range) {
		if(range > 1) {
			this._range = range;
		}
	}

	public StateBomb getStateBomb() {
		return this._stateBomb;
	}

	public void changeStateBomb() {
		switch(this._stateBomb) {
			case Step1:	this._stateBomb = StateBomb.Step2; break;
			case Step2: this._stateBomb = StateBomb.Step3; break;
			case Step3: this._stateBomb = StateBomb.Boom; break;
			case Boom: break;
		}
	}

}
