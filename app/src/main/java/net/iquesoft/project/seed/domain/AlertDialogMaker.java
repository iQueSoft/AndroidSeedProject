package net.iquesoft.project.seed.domain;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class AlertDialogMaker {
    private Context context;
    private String title;
    private String message;
    private String positiveButtonText;
    private String negativeButtonText;
    private int iconID;

    public AlertDialogMaker(Context context, String title, String body, String positiveButtonText, String negativeButtonString, int iconID) {
        this.context = context;
        this.title = title;
        this.message = body;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonString;
        this.iconID = iconID;
    }

    public AlertDialog getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(iconID)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
