package system;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.*;

import api.Space;
import api.Task;
import api.Shared;

public class SpaceImpl implements Space, Worker2Space, proxy{
	
	private static final Map waitMap = new ConcurrentHashMap();
	private static final BlockingQueue readyQ = new LinkedBlockingQueue();
	private static final BlockingQueue result = new LinkedBlockingQueue();
	private static final BlockingQueue proxyList = new LinkedBlockingQueue();
	private static int nextID = 1;
	private static final long serialVersionUID = 227L;
	private static Shared shared;
	
	public static void main(String[] args) {
		if (System.getSecurityManager() == null ) 
		{ 
		   System.setSecurityManager(new java.rmi.RMISecurityManager() ); 
		}
		try {
			Space space = new SpaceImpl();
			Space stub = (Space)UnicastRemoteObject.exportObject(space, 0);
			Registry registry = LocateRegistry.createRegistry( 1099 );
			registry.rebind(SERVICE_NAME, stub);
			System.out.println("SpaceImpl bound");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void put(Task task, Shared shared) throws RemoteException {
		
		//TODO create a shared variable here somwhere
		//this.shared = shared;
		
		//  Create a Closure object and insert to readyQ
		System.out.println("Task recieved...");
		Continuation finalResult = new Continuation(0,0);
		Closure root = new Closure(task, 0, finalResult, getID());
		readyQ.add(root);
	}
	
	@Override
	public Object take() throws RemoteException, InterruptedException {
		return result.take();
	}
	@Override
	public void exit() {
		while(!proxyList.isEmpty()){
			try {
				WorkerProxyImpl cpl = (WorkerProxyImpl)proxyList.take();
				cpl.exit();
				cpl.stop();
			} catch (Exception e) {
		
			}
			
		}
		System.exit(0);
		
		
	}
	public void putWaitMap(Closure closure) {
		//System.out.println("Placing closure in wait map...");
		waitMap.put(closure.ID, closure);
	}
	
	public void putQ(Closure closure) {
		//System.out.println("Putting closure to queue...");
		readyQ.add(closure);
	}
	
	synchronized public void placeArgument(Continuation cont, Object argument){
		//System.out.println("Placing argument to map key (ID) :" + cont.ID);
		if (cont.ID == 0){
			try {
				result.put(argument);
			} catch (InterruptedException e) {
				System.out.println("Could not return result");
			}
		}else{
			
			Closure closure = (Closure) waitMap.remove(cont.ID);
			Object [] temp =  (Object[])closure.args;
			temp[cont.argNumber] = argument;
			closure.joinCounter--;
			if (closure.joinCounter == 0){
				readyQ.add(closure);
			}else{
			putWaitMap(closure);
			}
		}
		
	}
	
	public Closure takeQ() throws InterruptedException {
			Closure closure = (Closure)readyQ.take();
			return closure;
	}
	
	public void register(Worker worker) throws RemoteException, InterruptedException {
		WorkerProxyImpl workerProxy = new WorkerProxyImpl(worker, this);
		workerProxy.start();
		proxyList.add(workerProxy);
		System.out.println("Computer registered");
	}
	
	public void unRegister(WorkerProxyImpl workerProxy) {
		proxyList.remove(workerProxy);
		System.out.println("Computer unregistered");
	}
	
	public synchronized int getID() { return nextID++; }
	
	

}
