package me.jershdervis.jloki.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Created by JershDervis on 28/07/2015.
 */
public class StreamReader extends Thread {

    private InputStream is;
    private StringWriter sw= new StringWriter();

    public StreamReader(InputStream is) {
        this.is = is;
    }

    public void run() {
        try {
            int c;
            while ((c = is.read()) != -1)
                sw.write(c);
        }
        catch (IOException e) {
        }
    }

    public String getResult() {
        return sw.toString();
    }
}
