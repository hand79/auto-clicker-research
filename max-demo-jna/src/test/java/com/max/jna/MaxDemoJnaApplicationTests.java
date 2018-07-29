package com.max.jna;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.cvkernels;
import org.bytedeco.javacpp.opencv_core.Cv64suf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.max.jna.service.JnaUser32ApiService;
import com.max.jna.type.HWndType;
import com.max.jna.type.UFlagsType;
import com.max.jna.util.ImageIOUtil;
import com.max.jna.util.MatUttil;
import com.max.jna.util.OpenCVUtil;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.GDI32Util;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.WINDOWINFO;
import com.sun.jna.ptr.IntByReference;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MaxDemoJnaApplicationTests {
	
	static {
        System.setProperty("java.awt.headless", "false");
	}

	@Autowired
	JnaUser32ApiService jnaUser32ApiService;
	
	@Test
	public void contextLoads() {
	}

//	@Test
	public void testJnaUser32ApiService() throws InterruptedException {
		DesktopWindow window = this.jnaUser32ApiService.findApplicationWindowsByTitle("緯軟學院", false);
		HWND hWnd = window.getHWND();
		WINDOWINFO windowInfo = this.jnaUser32ApiService.getWindowInfo(hWnd);
		IntByReference windowThreadProcessId = this.jnaUser32ApiService.getWindowThreadProcessId(hWnd);
		RECT rect = this.jnaUser32ApiService.getWindowRect(hWnd);
		Rectangle rectangle = this.jnaUser32ApiService.getWindowRectRectangle(hWnd);
//		HDC hdc = User32.INSTANCE.GetDC(hWnd);
		this.jnaUser32ApiService.doKeyboardByPostMessage(hWnd, KeyEvent.VK_F5, 2000);
		
		System.out.println(hWnd);
		System.out.println(windowInfo);
		System.out.println(windowThreadProcessId.getValue());
		System.out.println(rect);
		System.out.println(rectangle);
	}

//	@Test
	public void testCaptaure() throws AWTException, IOException{
		DesktopWindow window = this.jnaUser32ApiService.findApplicationWindowsByTitle("緯軟學院", false);
		HWND hWnd = window.getHWND();
		Rectangle screenRect = this.jnaUser32ApiService.getClientRectRectangle(hWnd);
		// use GDI32 window Captaure
		System.out.println(screenRect);
		BufferedImage image = GDI32Util.getScreenshot(hWnd);
		
		// all DesktopWindow
//		Robot robot = new Robot();
//		BufferedImage image = robot.createScreenCapture(screenRect);
		FileOutputStream fos = null;
		try {
			  fos = new FileOutputStream("D:\\Captaure.jpg");
	          ImageIO.write(image, "jpg", fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			 if(fos!=null){
	                fos.close();
	        }
		}
	}
	
//	@Test
	public void testWindowAdjustSize() throws AWTException, IOException{
		DesktopWindow window = this.jnaUser32ApiService.findApplicationWindowsByTitle("Google 新聞", false);
		HWND hWnd = window.getHWND();
		Rectangle rectRectangle = this.jnaUser32ApiService.getWindowRectRectangle(hWnd);
		this.jnaUser32ApiService.SetWindowPos(hWnd, HWndType.HWND_TOPMOST.getValue(), rectRectangle.x, rectRectangle.y, 100, 100, UFlagsType.SWP_NOZORDER.getValue());
	}
	
	
//	@Test
	public void testImageIO() {
		String readFormats[] = ImageIO.getReaderFormatNames();
		String writeFormats[] = ImageIO.getWriterFormatNames();
		String[] readerFileSuffixes = ImageIO.getReaderFileSuffixes();
		System.out.println("Readers:" + Arrays.asList(readFormats));
		System.out.println("Writers:" + Arrays.asList(writeFormats));
		System.out.println("readerFileSuffixes:" + Arrays.asList(readerFileSuffixes));
	}
	
//	@Test
	public void testFindPic() throws IOException{
		// read
		DesktopWindow window = this.jnaUser32ApiService.findApplicationWindowsByTitle("Google 新聞", false);
		HWND hWnd = window.getHWND();
		HDC hdc = this.jnaUser32ApiService.getDC(hWnd);
		Rectangle screenRect = this.jnaUser32ApiService.getClientRectRectangle(hWnd);
		
		// use GDI32 window Captaure
		BufferedImage scrImage = GDI32Util.getScreenshot(hWnd);
		System.out.println("oringinalWidth: " + scrImage.getWidth() + " oringinalHeight: " + scrImage.getHeight());

		// compare pic
		BufferedImage cutImage = ImageIOUtil.readImage(new File("D:\\cut.jpg"));
		System.out.println("cutWidth: " + cutImage.getWidth() + " cutHeight: " + cutImage.getHeight());
		
		// get Coordinate
		// 越接近0 越匹配
//		MinMaxLocResult minMaxLocResult = OpenCVUtil.findCoordinateByTmSqdiffNormed(scrImage, cutImage);
//		double thresholdMatch;
//		System.out.println("coordinate.minVal:"+ minMaxLocResult.minVal);
//		System.out.println("coordinate .minVal:"+ minMaxLocResult.maxVal);
//		int x = new BigDecimal(minMaxLocResult.minLoc.x).intValue();
//		int y = new BigDecimal(minMaxLocResult.minLoc.y).intValue();
		
		MinMaxLocResult minMaxLocResult = OpenCVUtil.findCoordinateByTmCcoeffNormed(scrImage, cutImage);
		double thresholdMatch;
		System.out.println("coordinate.minVal:"+ minMaxLocResult.minVal);
		System.out.println("coordinate.maxVal:"+ minMaxLocResult.maxVal);
		int x = new BigDecimal(minMaxLocResult.maxLoc.x).intValue();
		int y = new BigDecimal(minMaxLocResult.maxLoc.y).intValue();

		// gray
//        Mat gray = new Mat();
//        Imgproc.cvtColor(MatUttil.doBufferedImageToMat(scrImage), gray, Imgproc.COLOR_BGR2GRAY);
        
		System.out.println("x:" + x +" y:" + y);
		BufferedImage r = scrImage.getSubimage(x, y, cutImage.getWidth(), cutImage.getHeight());
		ImageIOUtil.writeImage(r, "jpg", new File("G:\\r.jpg"));
	}
	
//    //  For all the other methods, the higher the better
//    if( match_method  == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED )
//    { matchLoc = mmr.minLoc; }
//    else
//    { matchLoc = mmr.maxLoc; }
	
	
	//  TODO Need to check why it is not correct
//	private int[][] getImageRGB(BufferedImage image) {
//		int width = image.getWidth();
//		int height = image.getHeight();
//		int[][] result = new int[height][width];
//		for (int h = 0; h < height; h++) {
//			for (int w = 0; w < width; w++) {
//				// ARGB to RGB
//				// image.getRGB(x, y) is ARGB
//				result[h][w] = image.getRGB(w, h) & 0xFFFFFF;
//			}
//		}
//		return result;
//	}
//	
//
//	 public int[][][] findImage(BufferedImage scrImage, int[][] srcRGB, BufferedImage cutImage, int[][] cutRGB) {
//		 int srcHeight = scrImage.getHeight();
//		 int srcWidth = scrImage.getWidth();
//		 int cutHeight = cutImage.getHeight();
//		 int cutWidth = cutImage.getWidth();
//		 
//	     int[][][] findImageData = new int[cutHeight][cutWidth][2];
//	     //遍历屏幕截图像素点数据
//	     for(int y = 0; y < srcHeight - cutHeight; y++) {
//	       for(int x = 0; x < srcWidth- cutWidth; x++) {
//	         if((cutRGB[0][0] ^ srcRGB[y][x])==0
//	             && 
//	             (cutRGB[0][cutWidth-1] ^ srcRGB[y][x+cutWidth-1]) == 0
//	             &&
//	             (cutRGB[cutHeight-1][cutWidth-1] ^ srcRGB[y+cutHeight-1][x+cutWidth-1]) == 0
//	             &&
//	             (cutRGB[cutHeight-1][0] ^ srcRGB[y+cutHeight-1][x]) == 0) {
//	           
//	           boolean isFinded = this.isMatchAll(srcRGB, cutRGB, srcHeight, srcWidth, cutHeight, cutWidth, y, x);
//	           if(isFinded) {
//	             for(int h=0; h < cutHeight; h++) {
//	               for(int w=0; w < cutWidth; w++) {
//	                 findImageData[h][w][0] = y + h; 
//	                 findImageData[h][w][1] = x + w;
//	               }
//	             }
//	             return findImageData;
//	           }
//	         }
//	       }
//	     }
//		return findImageData;
//	   }
//	 
//	 public boolean isMatchAll(int[][] srcRGB, int[][] cutRGB, int srcHeight, int srcWidth, int cutHeight, int cutWidth, int y, int x) {
//	     // coordinate: src > cut 
//		 int srcY = 0;
//	     int srcX = 0;
//	     int xor = 0;
//	     for(int cutY = 0; cutY < cutHeight; cutY++) {
//	       srcY = y + cutY;
//	       for(int cutX = 0; cutX < cutWidth; cutX++) {
//	         srcX = x + cutX;
//	         if(srcY >= srcHeight || srcX >= srcWidth) {
//	           return false;
//	         }
//	         xor = cutRGB[cutY][cutX] ^ srcRGB[srcY][srcX];
//	         if(xor!=0) {
//	           return false;
//	         }
//	       }
//	       srcX = x;
//	     }
//	     return true;
//	   }
//	 
//	 private void printFindData(int[][][] findImageData, int cutHeight, int cutWidth) {
//	     for(int y= 0; y < cutHeight; y++) {
//	       for(int x= 0; x < cutWidth; x++) {
//	         System.out.print("("+findImageData[y][x][0]+", "+findImageData[y][x][1]+")");
//	       }
//	       System.out.println();
//	     }
//	 }
	 
	
}
