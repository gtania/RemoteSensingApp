package com.example.tania_nikos.remotesensing.Helpers;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Tania-Nikos on 21/2/2017.
 */

public class Spiner {

    protected static ProgressDialog nDialog;

    public static void show(Context $context)
    {
        nDialog = new ProgressDialog($context);
        nDialog.setMessage("Loading..");
        nDialog.setTitle("Φόρτωση");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    public static void dismiss()
    {
        nDialog.dismiss();
    }
}
