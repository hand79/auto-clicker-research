package com.max.jna.type;

public enum HWndType {
	// reference:
	// https://msdn.microsoft.com/en-us/library/windows/desktop/ms633545(v=vs.85).aspx
	HWND_BOTTOM(1), HWND_TOP(0), HWND_TOPMOST(-1), HWND_NOTOPMOST(-2);

	private int value;

	private HWndType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "" + this.value;
	}
}
