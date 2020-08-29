package ch.virt.traymodules.windowtracker.data;

import java.util.Calendar;
import java.util.Date;

/**
 * @author VirtCode
 * @version 1.0
 */
public class UsageData {
    private Date first;
    private Date last;
    private int total;

    public UsageData(){}
    public UsageData(Date first) {
        this.first = first;
    }

    public void setFirst(Date first) {
        this.first = first;
    }

    public void setLast(Date last) {
        this.last = last;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Date getFirst() {
        return first;
    }

    public Date getLast() {
        return last;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof UsageData){
            return first.getYear() == ((UsageData) obj).getFirst().getYear() && first.getMonth() == ((UsageData) obj).getFirst().getMonth() && first.getDate() == ((UsageData) obj).getFirst().getDate();
        }
        return false;
    }
}
