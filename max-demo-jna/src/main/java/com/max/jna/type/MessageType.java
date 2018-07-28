package com.max.jna.type;

public enum MessageType {
	
	// Keyboard
	WM_KEYDOWN(0x0100),
	WM_KEYUP(257),
	
	// mouse
	WM_LBUTTONUP(514),
	WM_LBUTTONDOWN(513),
	WM_LBUTTONDBLCLK(0x203);
	
    private int value;  

    private MessageType(int value) {  
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
