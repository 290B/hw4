package tasks;

import api.Shared;

public class SharedTsp implements Shared{
	private TspReturn shared;
	public SharedTsp(TspReturn input){
		shared = input;
	}
	public Object getShared(){
		return shared;
	}
	
	public void setShared(TspReturn input){
		shared = input;
	}

	public boolean isNewerThan(Object input) {
		TspReturn in = (TspReturn)input;
		if (in.getSumPathLength() < this.shared.getSumPathLength()){
			return true;
		}else{
			return false;
		}
	}
	
	public Object get() {
		return shared;
	}
	
	
	
}
