package com.commandapps.helloworldmap;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Michael on 10/11/2014.
 */
public class StorageUtil {

    private static final String SHARED_PREF_TAG = "SHARED_PREF";
    public static final String OFFICE_LOCATION_JSON_TAG = "OFFICE_LOCATION_JSON";


    public static void saveStringToPreferences(Context context, String tag, String str){
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREF_TAG, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(tag, str);
        editor.commit();
    }

    public static String getStringFromPreferences(Context context, String tag){
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREF_TAG, 0);
        return settings.getString(tag,"");
    }
}
