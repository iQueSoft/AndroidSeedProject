package net.iquesoft.project.seed.utils;

import android.util.Log;

public class LogUtil {

    public static void makeLog(String logMessage) {
        Log.d("Seed LOG", "=================================================================\n"
                + logMessage + "\n"
                + "=================================================================");
    }

    public static void makeLog(String tag, String logMessage) {
        Log.d(tag, logMessage);
    }


}
