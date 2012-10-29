package api;

public abstract class Shared implements SharedInterface{
	private Object shared;
	public Shared(Object input){
		shared = input;
	}
	public Object getShared(){
		return shared;
	}
	
	public void setShared(Object input){
		shared = input;
	}
	
}
