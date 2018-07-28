package com.max.jna.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class ImageIOUtil {
	// ImageIO surpport
	
	static{
		ImageIO.setUseCache(false);
		// not use to close 
		// ImageIO.setCacheDirectory()
	}
	
	public static BufferedImage readImage(File file) throws IOException{
		return ImageIO.read(file);
	}
	
	public static BufferedImage readImage(ImageInputStream stream) throws IOException{
		return ImageIO.read(stream);
	}
	
	public static BufferedImage readImage(InputStream stream) throws IOException{
		return ImageIO.read(stream);
	}
	
	public static BufferedImage readImage(URL url) throws IOException{
		return ImageIO.read(url);
	}
	
	public static boolean writeImage(RenderedImage image, String formatName, File file) throws IOException{
		return ImageIO.write(image, formatName, file);
	}
	
	public static boolean writeImage(RenderedImage image, String formatName, ImageOutputStream output) throws IOException{
		return ImageIO.write(image, formatName, output);
	}
	
	public static boolean writeImage(RenderedImage image, String formatName, OutputStream output) throws IOException{
		return ImageIO.write(image, formatName, output);
	}
	
	public static BufferedImage cutImage(RenderedImage image, int x, int y, int cutWidth, int cutHeight) throws IOException{
		BufferedImage screenshot = (BufferedImage) image;
		return screenshot.getSubimage(x, y, cutWidth, cutHeight);
	}
	
	
	public static String[] getReaderFormatNames(){
		return ImageIO.getReaderFormatNames();
	}
	
	public static String[] getReaderFileSuffixes(){
		return ImageIO.getReaderFileSuffixes();
	}
	
	public static String[] getReaderMIMETypes(){
		return ImageIO.getReaderMIMETypes();
	}
}
