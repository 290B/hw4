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

	private double tspShared;

	public SharedTsp(double input){
		tspShared = input;
	}
	
	public SharedTsp(){
		
	}
	
	public Object getShared(){
		return (double)tspShared;
	}

	public boolean isNewerThan(Shared input) {
		if ( (Double) input.getShared() <= this.tspShared){
			return false;
		}else{
			return true;
		}
	}
	
	public Shared clone() throws CloneNotSupportedException{
		return (Shared) super.clone();
	}
	
}
