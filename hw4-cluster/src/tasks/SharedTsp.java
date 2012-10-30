package tasks;

import java.io.Serializable;

import api.Shared;
/**This class is used for sharing the best pathlength found between workers and space in the DAC system
 * 
 * @author torgel
 *
 */
public class SharedTsp implements Shared, Serializable{
    static final long serialVersionUID = 227L; // Was missing 

	private TspReturn tspShared;

	public SharedTsp(TspReturn input){
		tspShared = input;
	}
	
	public SharedTsp(){
		
	}
	
	public Object getShared(){
		return tspShared;
	}

	public boolean isNewerThan(Shared input) {
		SharedTsp inputSharedTsp = (SharedTsp)input;
		TspReturn in = (TspReturn)inputSharedTsp.getShared();
		if (in.getSumPathLength() <= this.tspShared.getSumPathLength()){
			return false;
		}else{
			return true;
		}
	}
	
	public Shared clone() throws CloneNotSupportedException{
		return (Shared) super.clone();
	}
	
}
