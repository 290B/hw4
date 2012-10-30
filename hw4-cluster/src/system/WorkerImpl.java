package system;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import api.DAC;
import api.Shared;
import api.Task;

public class WorkerImpl implements Worker, Serializable {
    private Shared shared;
	private static Worker2Space space;
	private static final long serialVersionUID = 227L;
	
	public static void main(String[] args) {
		String spaceHost = args[0];
		String name = "Worker";
		String spaceName = "Space";
		if (System.getSecurityManager() == null ) 
		{ 
		   System.setSecurityManager(new java.rmi.RMISecurityManager()); 
		}
		try{
			Worker worker = new WorkerImpl();
			Worker stub = (Worker) UnicastRemoteObject.exportObject(worker, 0);
			//Registry registry = LocateRegistry.createRegistry( 1093 );
			//registry.rebind(name, stub);
			
			
			//Worker worker = new WorkerImpl();
			System.out.println("Connecting to space: " + spaceHost);
			Registry registrySpace = LocateRegistry.getRegistry(spaceHost);
			System.out.println("Looking up service: " + spaceName);
			space = (Worker2Space)registrySpace.lookup(spaceName);
			
			space.register(worker);
			System.out.println("WorkerImpl bound");
		} catch (Exception e) {
            System.err.println("WorkerImpl exception:");
            e.printStackTrace();
        }
	}
	
	public WorkerResult execute(Task task, Object [] args){
			DAC t = (DAC) task;
			if (t.args == null) t.args = args;
			t.setWorker(this);
			try {
				Shared sharedTemp = space.getShared(); 
				shared = sharedTemp.clone();
			} catch (RemoteException e) {
				System.out.println("Space could not send Shared to worker");
			} catch(CloneNotSupportedException e){
				System.out.println("Could not clone...");
			}
			task.execute();
			return new WorkerResult(t.spawn, t.spawn_next, t.spawn_nextJoin , t.send_argument);
	}
	public void exit() throws RemoteException {
		System.out.println("Remote call to exit()");
		System.exit(0);
		
	}
	public Shared getShared(){
		Shared shr;
		try {
			shr = shared.clone();
			return shr;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public void setShared(Shared proposedShared){
		if (proposedShared.isNewerThan(shared)){
			 try {
				System.out.println("Sending shared to space!!!");
				if (space.setShared(proposedShared)){
					System.out.println("Shared was send to space!!!"); 
					shared = proposedShared; 
				 }else{
					System.out.println("Shared was outdated");
					 shared = space.getShared();
				 }
			} catch (RemoteException e) {
				System.out.println("Could not send proposedShared to space");
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch(CloneNotSupportedException e){
				System.out.println("Could not clone...");
			}
				 
		}
	}
}
