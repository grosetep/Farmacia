package com.estrategiamovilmx.sales.farmacia.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.estrategiamovilmx.sales.farmacia.R;
import com.estrategiamovilmx.sales.farmacia.ui.interfaces.DialogCallbackInterface;

/**
 * Created by administrator on 27/06/2017.
 */
public class ShowConfirmations {
    private static final String TAG = ShowConfirmations.class.getSimpleName();
    private Activity activity;
    private DialogCallbackInterface actions;
    public static AlertDialog alertDialog;
    public ShowConfirmations(Activity activity, DialogCallbackInterface action){
        this.activity = activity;
        this.actions = action;
    }

    public static void showConfirmationMessage(final String message,final Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,message,Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void showConfirmationMessageShort(final String message,final Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void openRetry(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(activity.getResources().getString(R.string.no_connection));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("REINTENTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                    actions.method_positive();

            }
        });

        alertDialogBuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    actions.method_negative(activity);
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }






}
