package api;

public interface Shared {

	public boolean isNewerThan(Object input);
	
	public Object getShared();
	
	public Shared clone();
}
