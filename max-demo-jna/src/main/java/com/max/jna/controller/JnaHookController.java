package com.max.jna.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.max.jna.hook.KeyboardHook;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.WinDef.HWND;

@RestController
@RequestMapping(path = "jna")
public class JnaHookController {

	private static KeyboardHook keyboardHook;
	private static HWND hWnd;
	
	@RequestMapping(path = "/run")
	public void testJnaMap() {
		String windowTitle = new String("Google");
		DesktopWindow window = null;
		
		List<DesktopWindow> wins = WindowUtils.getAllWindows(false);
		for (DesktopWindow desktopWindow : wins) {
			if (desktopWindow.getTitle().contains(windowTitle)){
				window = desktopWindow;
				break;
			}
		}
		hWnd = window.getHWND();
		keyboardHook = new KeyboardHook(hWnd, "D://capture.jpg");
		keyboardHook.run();
	}

	@RequestMapping(path = "/hookOff")
	public void testHookoff() {
		keyboardHook.setHookOff();
	}
}
