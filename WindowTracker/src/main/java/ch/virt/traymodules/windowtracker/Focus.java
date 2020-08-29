package ch.virt.traymodules.windowtracker;


import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;

import static com.sun.jna.platform.win32.WinNT.PROCESS_VM_READ;

/**
 * @author VirtCode
 * @version 1.0
 */
public class Focus {
    private static final int MAX_TITLE_LENGTH = 1024;

    public static String getWindowText(){
        char[] buffer = new char[MAX_TITLE_LENGTH * 2];
        User32.INSTANCE.GetWindowText(User32.INSTANCE.GetForegroundWindow(), buffer, MAX_TITLE_LENGTH);
        return Native.toString(buffer);
    }

    public static String getWindowProcess(){
        char[] buffer = new char[MAX_TITLE_LENGTH * 2];
        IntByReference pointer = new IntByReference();
        User32.INSTANCE.GetWindowThreadProcessId(User32.INSTANCE.GetForegroundWindow(), pointer);
        WinNT.HANDLE process = com.sun.jna.platform.win32.Kernel32.INSTANCE.OpenProcess(WinNT.PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, false, pointer.getValue());
        Psapi.INSTANCE.GetModuleFileNameExW(process, null, buffer, MAX_TITLE_LENGTH);
        return Native.toString(buffer);
    }
}
