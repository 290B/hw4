package client;

import java.rmi.RemoteException;

import api.Task;
import system.WorkerImpl;

public class Worker extends WorkerImpl {

	@Override
	public <T> T execute(Task t, Object[] args) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
