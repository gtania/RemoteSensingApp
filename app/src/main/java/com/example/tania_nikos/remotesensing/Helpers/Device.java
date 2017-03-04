package com.example.tania_nikos.remotesensing.Helpers;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Tania-Nikos on 12/2/2017.
 */

public class Device {
    public static String getId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
