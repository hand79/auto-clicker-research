package com.max.jna.service;

import java.awt.Rectangle;
import java.util.List;

import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser.WINDOWINFO;
import com.sun.jna.ptr.IntByReference;


public interface JnaUser32ApiService{
	
	public User32 getUser32Instance();

	public DesktopWindow findApplicationWindowsByTitle(String windowTitle, boolean onlyVisibleWindows);

	public List<DesktopWindow> findApplicationWindows(boolean onlyVisibleWindows);

	public HWND getActiveWindow();

	public WINDOWINFO getWindowInfo(HWND hWnd);

	public IntByReference getWindowThreadProcessId(HWND hWnd);

	public HDC getDC(HWND hWnd);

	public RECT getWindowRect(HWND hWnd);

	public Rectangle getWindowRectRectangle(HWND hWnd);
	
	public RECT getClientRect(HWND hWnd);
	
	public Rectangle getClientRectRectangle(HWND hWnd);
	
	public void doMouseByPostMessage(HWND hWnd, int x, int y, int threadsleep) throws InterruptedException;

	public void doKeyboardByPostMessage(HWND hWnd, int pressKey, int threadsleep) throws InterruptedException;

	public boolean isWindow(HWND hWnd);

	public boolean isWindowEnabled(HWND hWnd);

	public boolean isWindowVisible(HWND hWnd);

	public boolean SetWindowPos(HWND hWnd, int hWndInsertAfter, int X, int Y, int width, int height, int uFlags);
	
	public boolean SetWindowPos(HWND hWnd, int hWndInsertAfter, int X, int Y, double width, double height, int uFlags);

	public boolean adjustWindowSize(HWND hWnd, int width, int height);

	public boolean moveWindow(HWND hWnd, int x, int y);

	public int releaseDC(HWND hWnd);




}
