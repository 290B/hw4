package system;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import api.DAC;
import api.Shared;
import api.Task;

public class WorkerImpl implements Worker {
	Shared shared;
	static Worker2Space space;
	
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
			try {
				shared = space.getShared();
			} catch (RemoteException e) {
				System.out.println("Space could not send Shared to worker");
			}
			task.execute(shared);
			return new WorkerResult(t.spawn, t.spawn_next, t.spawn_nextJoin , t.send_argument);
	}
	public void exit() throws RemoteException {
		System.out.println("Remote call to exit()");
		System.exit(0);
		
	}
	public Shared getShared(){return shared;}
}
