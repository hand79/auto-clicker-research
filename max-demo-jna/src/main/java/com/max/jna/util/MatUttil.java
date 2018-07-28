package com.max.jna.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

public class MatUttil {

	public static Mat doBufferedImageToMat(BufferedImage image, int imgType, int matType) {
		// Don't convert if it already has correct type
		if (image.getType() != imgType) {

			// Create a buffered image
			BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), imgType);

			// Draw the image onto the new buffer
			Graphics2D g = bufferedImage.createGraphics();
			try {
				g.setComposite(AlphaComposite.Src);
				g.drawImage(image, 0, 0, null);
			} finally {
				g.dispose();
			}
		}

		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat mat = Mat.eye(image.getHeight(), image.getWidth(), imgType);
		mat.put(0, 0, pixels);
		return mat;
	}
	
	
	public static BufferedImage doMatToBufferedImage(Mat matrix){
		return (BufferedImage) HighGui.toBufferedImage(matrix);
	}
	
	// V2
	public static Mat doBufferedImageToMat(BufferedImage image) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", byteArrayOutputStream);
		byteArrayOutputStream.flush();
		return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
	}
	
	public static BufferedImage doMatToBufferedImageV2(Mat matrix) throws IOException {
		MatOfByte mob = new MatOfByte();
		Imgcodecs.imencode(".jpg", matrix, mob);
		return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
	}
}
