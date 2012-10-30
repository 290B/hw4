package api;

public interface Shared extends Cloneable{

	public boolean isNewerThan(Object input);
	
	public Object getShared();
	
	public Shared clone()throws CloneNotSupportedException;
}
