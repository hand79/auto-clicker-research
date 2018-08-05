package com.max.jna.hook;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.sun.jna.platform.win32.GDI32Util;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;

public class KeyboardHook implements Runnable{
	private static volatile boolean quit;
	private static HHOOK hhk;
	private HWND hWnd;
	private String outputPath;
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 
	// reference: http://www.hsfzxjy.site/sharing-a-keyboard-hook-in-delphi/
	public KeyboardHook(HWND hWnd, String outputPath) {
		this.hWnd = hWnd;
		this.outputPath = outputPath;
	}
	
	public HWND gethWnd() {
		return hWnd;
	}
	public void sethWnd(HWND hWnd) {
		this.hWnd = hWnd;
	}
	
	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public void run() {
		setHookOn();
	}
	
	// hook proc 
	private LowLevelKeyboardProc keyboardProc = new LowLevelKeyboardProc() {
		@Override
		public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT event) {
			// press key & time
			if (nCode >= 0) {
				String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				// presss F9 unhook
				if(KeyEvent.VK_F9 == event.vkCode) {
					System.out.println(sdf.format(new Date()) + " KEY: " + event.vkCode);
					quit = true;
					KeyboardHook.this.setHookOff();
				}
				if(KeyEvent.VK_F8 == event.vkCode && WinUser.WM_KEYUP == wParam.intValue()) {
					System.out.println(sdf.format(new Date()) + " KEY: " + event.vkCode);
					BufferedImage image = GDI32Util.getScreenshot(hWnd);
					outputImgae(image, outputPath);
				}
				while (!quit) {
					System.out.println("我還在跑");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return User32.INSTANCE.CallNextHookEx(hhk, nCode, wParam, null);
		}
	};
 
	// install hook
	public void setHookOn(){
		System.out.println("Hook On!");
 
		HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
		hhk = User32.INSTANCE.SetWindowsHookEx(User32.WH_KEYBOARD_LL, keyboardProc, hMod, 0);
 
		int result;
		MSG msg = new MSG();
		while ((result = User32.INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
			if (result == -1) {
				setHookOff();
				break;
			} else {
				User32.INSTANCE.TranslateMessage(msg);
				User32.INSTANCE.DispatchMessage(msg);
			}
		}
	}
	// remove hook & exit application
	public void setHookOff(){
		System.out.println("Hook Off!");
		quit = true;
		User32.INSTANCE.UnhookWindowsHookEx(hhk);
//		System.exit(0);
	}
	
	private void outputImgae(BufferedImage image, String outputPath){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outputPath);
			ImageIO.write(image, "jpg", fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
		}finally {
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					System.out.println(e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

