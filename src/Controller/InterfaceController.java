package Controller;

public interface InterfaceController {

	public void start();
	public void step();
	public void run();
	public void stop();
	public void setTime(long time);
	public long getTime();
	public int getInitTime();
}
