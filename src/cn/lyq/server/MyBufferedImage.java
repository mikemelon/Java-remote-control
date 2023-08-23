package cn.lyq.server;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class MyBufferedImage extends BufferedImage implements Serializable {

	private static final long serialVersionUID = -8742590436154744258L;
	
	private BufferedImage bufferedImage;
	
	public MyBufferedImage() {
		super(100, 100, BufferedImage.TYPE_INT_RGB);
	}

	public MyBufferedImage(int width, int height, int imageType) {
		super(width, height, imageType);
	}
	
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage =bufferedImage;
	}

//	private void writeObject(ObjectOutputStream out) throws IOException {
//		out.defaultWriteObject();
//	}
//
//	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//		in.defaultReadObject();
//	}
}
