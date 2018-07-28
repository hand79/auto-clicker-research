package com.max.jna.service;

import java.awt.Rectangle;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.max.jna.type.HWndType;
import com.max.jna.type.MessageType;
import com.max.jna.type.UFlagsType;
import com.sun.jna.Pointer;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.WINDOWINFO;
import com.sun.jna.ptr.IntByReference;

@Service
public class JnaUser32ApiServiceImpl implements JnaUser32ApiService{
	
	private User32 instance = User32.INSTANCE;
	
	@Override
	public User32 getUser32Instance(){
		return this.instance;
	}
	
	@Override
	public DesktopWindow findApplicationWindowsByTitle(String windowTitle, boolean onlyVisibleWindows) {
		List<DesktopWindow> filterWindows = WindowUtils.getAllWindows(onlyVisibleWindows).stream()
		.filter(p -> p.getTitle().contains(windowTitle))
		.collect(Collectors.toList());
		return filterWindows.isEmpty() ? null : filterWindows.get(0);
	}

	@Override
	public List<DesktopWindow> findApplicationWindows(boolean onlyVisibleWindows) {
		List<DesktopWindow> desktopWindows = WindowUtils.getAllWindows(onlyVisibleWindows);
		return desktopWindows;
	}

	@Override
	public HWND getActiveWindow() {
		return this.instance.GetActiveWindow();
	}
	
	@Override
	public WINDOWINFO getWindowInfo(HWND hWnd) {
		WINDOWINFO pwi = new WINDOWINFO();
		this.instance.GetWindowInfo(hWnd, pwi);
		return pwi;
	}

	@Override
	public IntByReference getWindowThreadProcessId(HWND hWnd) {
		IntByReference lpdwProcessId = new IntByReference();
		this.instance.GetWindowThreadProcessId(hWnd, lpdwProcessId);
		return lpdwProcessId;
	}

	@Override
	public HDC getDC(HWND hWnd) {
		// if hWnd is null, capture the entire screen
		return this.instance.GetDC(hWnd);
	}

	@Override
	public RECT getWindowRect(HWND hWnd) {
		// origin coordinate: screen's upper left corner
		RECT rect = new RECT();
		boolean succeed = this.instance.GetWindowRect(hWnd, rect);
		return succeed ? rect : null;
	}

	@Override
	public Rectangle getWindowRectRectangle(HWND hWnd) {
		RECT rect = this.getWindowRect(hWnd);
		return rect != null ? rect.toRectangle() : null;
	}
	
	@Override
	public RECT getClientRect(HWND hWnd) {
		// origin coordinate: application window's upper left corner
		RECT rect = new RECT();
		boolean succeed = this.instance.GetClientRect(hWnd, rect);
		return succeed ? rect : null;
	}

	@Override
	public Rectangle getClientRectRectangle(HWND hWnd) {
		// origin coordinate: application window's upper left corner
		RECT rect = this.getClientRect(hWnd);
		return rect != null ? rect.toRectangle() : null;
	}
	
	@Override
	public void doMouseByPostMessage(HWND hWnd, int x, int y, int threadsleep) throws InterruptedException {
		WPARAM wParam = new WPARAM(0);
		// window's coordinate
		User32.INSTANCE.SetForegroundWindow(hWnd);
		LPARAM lParam = this.makeLParam(x, y);
		User32.INSTANCE.PostMessage(hWnd, MessageType.WM_LBUTTONDOWN.getValue(), wParam, lParam);
		User32.INSTANCE.PostMessage(hWnd, MessageType.WM_LBUTTONUP.getValue(), wParam, lParam);
		Thread.sleep(threadsleep);
		
	}
	
	private long makeLong(int a, int b) {
		int wordShift = 16;	
		return a | (b << wordShift);
	}
	
	private LPARAM makeLParam(int a, int b) {
		LPARAM lParam = new LPARAM(this.makeLong(a, b));
		return lParam;
	}

	@Override
	public void doKeyboardByPostMessage(HWND hWnd, int pressKey, int threadsleep) throws InterruptedException {
		// pressKey:  ex: KeyEvent.Vk_F5
		WPARAM wParam = new WPARAM(pressKey);
		LPARAM lParam = new LPARAM(0);
		User32.INSTANCE.SetForegroundWindow(hWnd);
		User32.INSTANCE.PostMessage(hWnd, MessageType.WM_KEYDOWN.getValue(), wParam, lParam);
		User32.INSTANCE.PostMessage(hWnd, MessageType.WM_KEYUP.getValue(), wParam, lParam);
		Thread.sleep(threadsleep);
	}
	
	
	@Override
	public boolean isWindow(HWND hWnd) {
		return this.instance.IsWindow(hWnd);
	}
	
	@Override
	public boolean isWindowEnabled(HWND hWnd) {
		return this.instance.IsWindowEnabled(hWnd);
	}
	
	@Override
	public boolean isWindowVisible(HWND hWnd) {
		return this.instance.IsWindowVisible(hWnd);
	}
	
	@Override
	public boolean SetWindowPos(HWND hWnd, int hWndInsertAfter, int X, int Y, int width, int height, int uFlags) {
		// height & width   unit: pixel
		// X,Y  position of the window, in client coordinates.
		return this.instance.SetWindowPos(hWnd, new HWND(new Pointer(hWndInsertAfter)), X, Y, width, height, uFlags);
	}
	
	@Override
	public boolean adjustWindowSize(HWND hWnd, int width, int height){
		Rectangle rectRectangle = this.getWindowRectRectangle(hWnd);
		return this.SetWindowPos(hWnd, HWndType.HWND_TOPMOST.getValue(), rectRectangle.x, rectRectangle.y, width, height, UFlagsType.SWP_NOZORDER.getValue());
	}
	
	@Override
	public boolean moveWindow(HWND hWnd, int x, int y){
		Rectangle rectRectangle = this.getWindowRectRectangle(hWnd);
		return this.SetWindowPos(hWnd, HWndType.HWND_TOPMOST.getValue(), x, y, rectRectangle.width, rectRectangle.height, UFlagsType.SWP_NOSIZE.getValue());
	}
	
}
