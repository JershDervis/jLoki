package me.jershdervis.jloki.steal;

/**
 * Created by JershDervis on 28/07/2015.
 */
public abstract class Stealer {

    private final String name;

    public Stealer(String name) {
        this.name = name;
    }

    /**
     * Executes the main method of stealer
     * @return
     */
    public abstract String[] run();

    /**
     * Returns the name of the stealer object
     * @return
     */
    public String getName() {
        return this.name;
    }
}
