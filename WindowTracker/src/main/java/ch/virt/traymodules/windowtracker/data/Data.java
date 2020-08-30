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
    private ArrayList<ProcessData> data = new ArrayList<>();

    public void usedProcess(int seconds, String process, String name){
        if (data.contains(new ProcessData(process))){
            ProcessData data = this.data.get(this.data.indexOf(new ProcessData(process)));
            data.currentlyUsedFor(seconds, name);
        }else {
            ProcessData data = new ProcessData(process);
            data.currentlyUsedFor(seconds, name);
            this.data.add(data);
        }
    }
}
