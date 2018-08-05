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
import org.springframework.util.ResourceUtils;

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

	@Test
	public void testCaptaure() throws AWTException, IOException{
		DesktopWindow window = this.jnaUser32ApiService.findApplicationWindowsByTitle("かんぱに☆ガールズ", false);
		HWND hWnd = window.getHWND();
		Rectangle screenRect = this.jnaUser32ApiService.getClientRectRectangle(hWnd);
		System.out.println(screenRect);
		Rectangle screenRect2 = this.jnaUser32ApiService.getWindowRectRectangle(hWnd);
		System.out.println(screenRect2);

		// use GDI32 window Captaure
		BufferedImage image = GDI32Util.getScreenshot(hWnd);
//		GDI32.INSTANCE.DeleteObject(hBitmap);
		// all DesktopWindow
//		Robot robot = new Robot();
//		BufferedImage image = robot.createScreenCapture(screenRect);
		FileOutputStream fos = null;
		try {
			  fos = new FileOutputStream("G:\\Captaure1.jpg");
	          ImageIO.write(image, "jpg", fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			 if(fos!=null){
	                fos.close();
	                this.jnaUser32ApiService.releaseDC(hWnd);
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
	
	@Test
	public void testOpenCV() throws InterruptedException, IOException{
		DesktopWindow window = this.jnaUser32ApiService.findApplicationWindowsByTitle("google", false);
		HWND hWnd = window.getHWND();
		HDC hdc = this.jnaUser32ApiService.getDC(hWnd);
		User32.INSTANCE.SetForegroundWindow(hWnd);
		// use GDI32 window Captaure
		BufferedImage scrImage = GDI32Util.getScreenshot(hWnd);
		System.out.println("windowWidth: " + scrImage.getWidth() + " windowHeight: " + scrImage.getHeight());
		ImageIOUtil.writeImage(scrImage, "jpg", new File("G://test.jpg"));
//		Rectangle screenRect = this.jnaUser32ApiService.getClientRectRectangle(hWnd);
		// title
		// find title pic
//		BufferedImage titleImage = this.findPic("window/title.jpg");
//		ImageIOUtil.writeImage(titleImage, "jpg", new File("G://test.jpg"));
//		MinMaxLocResult mlrTitle = OpenCVUtil.findCoordinateByTmSqdiffNormed(scrImage, titleImage);
//		System.out.println(mlrTitle.minVal);
//		System.out.println(mlrTitle.minLoc.x);
//		System.out.println(mlrTitle.minLoc.y);
//		boolean checkTitle = OpenCVUtil.isMatchTmSqdiffNormed(mlrTitle, 0.05);
//		System.out.println(checkTitle);
//		if(checkTitle){
//			int titleX = new BigDecimal(mlrTitle.maxLoc.x).intValue();
//			int titleY = new BigDecimal(mlrTitle.maxLoc.y).intValue();
//			this.jnaUser32ApiService.doMouseByPostMessage(hWnd, titleX, titleY, 500);
//			this.jnaUser32ApiService.doMouseByPostMessage(hWnd, titleX, titleY, 500);
//			
//			BufferedImage monsterImage = this.findPic("monster/1.jpg");
//			MinMaxLocResult mlrMonster = OpenCVUtil.findCoordinateByTmCcoeffNormed(scrImage, monsterImage);
//			boolean checkMonster = OpenCVUtil.isMatchTmCcoeffNormed(mlrMonster, 0.9);
//			if(checkMonster){
//				// skill
//				this.jnaUser32ApiService.doKeyboardByPostMessage(hWnd, KeyEvent.VK_3, 800);
//				this.jnaUser32ApiService.doKeyboardByPostMessage(hWnd, KeyEvent.VK_2, 800);
//				this.jnaUser32ApiService.doKeyboardByPostMessage(hWnd, KeyEvent.VK_1, 800);
//				Thread.sleep(12000);
//			} else {
//				Thread.sleep(3000);
//			}
//		}
	}
	
	private BufferedImage findPic(String filePath) throws IOException{
		File file = ResourceUtils.getFile("classpath:clansenki/"+filePath);
		BufferedImage image = ImageIOUtil.readImage(file);
		return image;
	}
	
//    //  For all the other methods, the higher the better
//    if( match_method  == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED )
//    { matchLoc = mmr.minLoc; }
//    else
//    { matchLoc = mmr.maxLoc; }
	
}
