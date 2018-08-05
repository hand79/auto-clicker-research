package com.max.jna.service;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;

import com.sun.jna.platform.win32.GDI32Util;
import com.sun.jna.platform.win32.WinDef.HWND;

@Service
public class JnaGDI32ServiceImpl implements JnaGDI32ApiService  {

	@Override
	public BufferedImage getWindowScreenshot(HWND hWnd) {
		return GDI32Util.getScreenshot(hWnd);
	}

}
