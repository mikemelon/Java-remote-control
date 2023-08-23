package cn.lyq;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import cn.lyq.server.RemoteControlInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class Controller {
	@FXML
	private Button captureScreenBtn;
	@FXML
	private Button mouseSpinBtn;
	@FXML
	private Button execCmdBtn;
	
	private String remoteIP="127.0.0.1";
	
	@FXML
	public void onCaptureScreenBtn(ActionEvent e) {//远程截屏, 并用本地“画图”打开
		RemoteControlInterface remoteControlObj = getRemoteControlObj();
		if(remoteControlObj != null) {
			try {
				byte[] jpgBytes = remoteControlObj.remoteCaptureScreen();
				// BufferedImage没有实现Serializable，网络传输需转为byte[]，这里再转回文件
				System.out.println("jpgBytes="+jpgBytes+",len="+jpgBytes.length);

				try(BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream("screenshot.jpg"))){
					bos.write(jpgBytes);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				Runtime.getRuntime().exec("mspaint screenshot.jpg");
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		} else {
			showRemoteConnectErrorAlert();
		}
	}
	
	@FXML
	public void onMouseSpinBtn(ActionEvent e) {// 远程晃圈鼠标
		RemoteControlInterface remoteControlObj = getRemoteControlObj();
		if(remoteControlObj != null) {
			boolean ok =false;
			try {
				ok = remoteControlObj.remoteMouseSpin(10);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} 
			System.out.println("远程执行成功了吗？"+ok);
		} else {
			showRemoteConnectErrorAlert();
		}
	}
	
	@FXML
	public void onExecCmdBtn(ActionEvent e) {  // 远程执行命令
		RemoteControlInterface remoteControlObj = getRemoteControlObj();
		if(remoteControlObj != null) {
			boolean ok=false;
			try {
				String cmdString = JOptionPane.showInputDialog(null, "请输入你要远程执行的命令字符串："); //输入框，是一个阻塞方法
				// 可以尝试的命令：
				// (1)notepad     打开记事本
				// (2)calc        打开计算器
				// (3)cmd /c shutdown /i /r   远程重启（/i表示图形界面，可取消。去掉后不显示，慎用！）
				ok = remoteControlObj.remoteExecCmd(cmdString);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			System.out.println("远程执行成功了吗？"+ok);
		} else {
			showRemoteConnectErrorAlert();
		}
	}
	
	@FXML
	public void onChangeServerIPLink(ActionEvent e) { //修改服务器IP地址
		remoteIP = JOptionPane.showInputDialog(null, "请输入IP地址");
	}

	// 显示"远程连接错误"对话框
	private void showRemoteConnectErrorAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("无法连接上远程服务器");
		alert.setContentText("请检查远程服务器是否正确运行");
		alert.showAndWait();
	}
	
	// 获取远程服务对象
	private RemoteControlInterface getRemoteControlObj() {
		RemoteControlInterface remoteControlObj = null;
		try {
			Remote rmiObj = Naming.lookup("rmi://"+remoteIP+"/remotecontrol");
			remoteControlObj = (RemoteControlInterface)rmiObj;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return remoteControlObj;
	}
}
