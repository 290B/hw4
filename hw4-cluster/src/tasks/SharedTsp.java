package tasks;

import api.Shared;
/**This class is used for sharing the best pathlength found between workers and space in the DAC system
 * 
 * @author torgel
 *
 */
public class SharedTsp implements Shared, Cloneable{
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
	
	protected Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
	
}
