package DeCell.GraphicsExtra.Helpers;

import com.fs.starfarer.api.Global;

public class Logger {

    public static boolean loggerEnabled = Global.getSettings().getBoolean("FAE_LoggerEnabled");

    /**
     * logs stuff. <br/>
     * returns true if logged, othervise false
     */
    public static void log(Class myClass, String val1, String val2, String val3) {
        if (!loggerEnabled) return;
        Global.getLogger(myClass).info(val1 + val2 + val3);
    }

    /**
     * logs stuff. <br/>
     * returns true if logged, othervise false
     */
    public static void log(Class myClass, String val1, String val2) {
        if (!loggerEnabled) return;
        Global.getLogger(myClass).info(val1 + val2);
    }

    /**
     * logs stuff. <br/>
     * returns true if logged, othervise false
     */
    public static void log(Class myClass, String val1) {
        if (!loggerEnabled) return;
        Global.getLogger(myClass).info(val1);
    }

}
