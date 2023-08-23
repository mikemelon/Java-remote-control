package cn.lyq.server;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.imageio.ImageIO;

public class RemoteControlConcrete extends UnicastRemoteObject implements RemoteControlInterface {

	private static final long serialVersionUID = 6301520888438992318L;

	protected RemoteControlConcrete() throws RemoteException {
	}

	@Override
	public boolean remoteExecCmd(String cmd) throws RemoteException {
		try {
			// 可以尝试的命令：
			// (1)notepad     打开记事本
			// (2)calc        打开计算器
			// (3)cmd /c shutdown /i /r   远程重启（/i表示图形界面，可取消。去掉后不显示，慎用！）
			Runtime.getRuntime().exec(cmd); 
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public byte[] remoteCaptureScreen() throws RemoteException {
		File screenShotFile = null;
		try {
			Robot robot = new Robot();
			BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			screenShotFile = new File("screenshot.jpg");
			ImageIO.write(bufferedImage,"jpg",screenShotFile);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if(screenShotFile!=null) {
			try(BufferedInputStream bis= new BufferedInputStream(new FileInputStream(screenShotFile)); 
				ByteArrayOutputStream bos=new ByteArrayOutputStream();) {
	
				byte[] bytes = new byte[1024];
				int len = -1;
				while(( len=bis.read(bytes) )!=-1) {
					bos.write(bytes, 0, len);
				}
				
				return bos.toByteArray(); // BufferedImage没有实现Serializable，改为传输byte[]
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return null;
	}

	@Override
	public boolean remoteMouseSpin(int count) throws RemoteException {
		try {
			Robot robot = new Robot();
			//圆心坐标(centerX, CenterY) ，半径radius, 单位：像素
			int centerX = 500;
			int centerY = 500;
			int radius = 200;

			for (int n = 0; n < count; n++) { // 画count圈
				for (int degree = 0; degree < 360; degree++) {
					double rad = degree * Math.PI / 180;
					int moveX = (int) (centerX + radius * Math.cos(rad));
					int moveY = (int) (centerY + radius * Math.sin(rad));
					System.out.println("(" + moveX + "," + moveY + ")");
					robot.mouseMove(moveX, moveY);
					Thread.sleep(1);
				}
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
