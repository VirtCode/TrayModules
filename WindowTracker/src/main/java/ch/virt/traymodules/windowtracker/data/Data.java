package ch.virt.traymodules.windowtracker.data;

import java.security.cert.PolicyQualifierInfo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author VirtCode
 * @version 1.0
 */
public class Data {
    private ArrayList<ProcessData> data;

    public void usedProcess(int minutes, String process, String name){
        if (data.contains(new ProcessData(process))){
            ProcessData data = this.data.get(this.data.indexOf(new ProcessData(process)));
            data.currentlyUsedFor(minutes, name);
        }else {
            ProcessData data = new ProcessData(process);
            data.currentlyUsedFor(minutes, name);
            this.data.add(data);
        }
    }
}
