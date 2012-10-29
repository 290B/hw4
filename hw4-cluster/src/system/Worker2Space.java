package system;




public interface Worker2Space extends java.rmi.Remote
{
    void register(Worker worker) throws java.rmi.RemoteException, InterruptedException;
}