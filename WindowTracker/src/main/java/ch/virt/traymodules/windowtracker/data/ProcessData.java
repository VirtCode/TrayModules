package ch.virt.traymodules.windowtracker.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author VirtCode
 * @version 1.0
 */
public class ProcessData {
    private String executable = "";
    private ArrayList<UsageData> usages = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();

    public ProcessData(){}
    public ProcessData(String executable) {
        this.executable = executable;
    }

    public String getExecutable() {
        return executable;
    }

    public ArrayList<UsageData> getUsages() {
        return usages;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    public void setUsages(ArrayList<UsageData> usages) {
        this.usages = usages;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public void currentlyUsedFor(int seconds, String name){
        if(name != null) if (!names.contains(name)) names.add(name);

        Date date = Calendar.getInstance().getTime();
        if (usages.contains(new UsageData(date))){
            UsageData data = usages.get(usages.indexOf(new UsageData(date)));

            data.setLast(date);
            data.setTotal(data.getTotal() + seconds);
        }else {
            UsageData data = new UsageData(date);
            data.setTotal(seconds);
            usages.add(data);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof ProcessData && executable.equals(((ProcessData) obj).getExecutable()));
    }
}
