package com.max.jna.type;

public enum UFlagsType {
	// https://msdn.microsoft.com/zh-tw/library/windows/desktop/ms633545(v=vs.85).aspx
	SWP_ASYNCWINDOWPOS(0x4000),
	SWP_DEFERERASE(0x2000),
	SWP_DRAWFRAME(0x0020),
	SWP_FRAMECHANGED(0x0020),
	SWP_HIDEWINDOW(0x0080),
	SWP_NOACTIVATE(0x0010),
	SWP_NOCOPYBITS(0x0100),
	SWP_NOMOVE(0x0002),
	SWP_NOOWNERZORDER(0x0200),
	SWP_NOREDRAW(0x0008),
	SWP_NOREPOSITION(0x0200),
	SWP_NOSENDCHANGING(0x0400),
	SWP_NOSIZE(0x0001),
	SWP_NOZORDER(0x0004),
	SWP_SHOWWINDOW(0x0040)
	;
    private int value;  

    private UFlagsType(int value) {  
        this.value = value;  
    }
    
    public int getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
    	return ""+this.value;
    }
}
