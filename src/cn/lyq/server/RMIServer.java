package cn.lyq.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
/**
 * 一定要提前运行 rmiregistry这个命令后，再执行本程序，需注意：
 * 
 * （1）rmiregistry命令是JDK自带的命令（如果运行不了，检查PATH里是否设置有 "<JDK install path>\bin"目录
 * 
 * （2）rmiregistry需要在能搜索到本类的目录下运行，对于Eclipse工程来说，就是工程的bin子目录
 * 
 * @author lyq
 *
 */
public class RMIServer {

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		RemoteControlConcrete  rmiObj = new RemoteControlConcrete();
		Naming.rebind("rmi://127.0.0.1/remotecontrol",rmiObj); 
	}

}
