package com.max.jna.service;

import java.awt.image.BufferedImage;

import com.sun.jna.platform.win32.WinDef.HWND;

public interface JnaGDI32ApiService{
	
	
	public BufferedImage getWindowScreenshot(HWND hWnd);
}
