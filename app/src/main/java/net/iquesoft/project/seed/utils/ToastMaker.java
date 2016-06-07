package net.iquesoft.project.seed.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastMaker {

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
