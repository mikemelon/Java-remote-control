package cn.lyq.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;

public interface RemoteControlInterface extends Remote {
	public byte[] remoteCaptureScreen() throws RemoteException;
	public boolean remoteMouseSpin(int count) throws RemoteException;
	public boolean remoteExecCmd(String cmd) throws RemoteException;
}
