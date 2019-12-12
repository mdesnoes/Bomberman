package Controller;

import Model.BombermanGame;

public interface InterfaceController {

	void start();
	void step();
	void run();
	void stop();
	void setTime(long time);
	long getTime();
	int getInitTime();

}
