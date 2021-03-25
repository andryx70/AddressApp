package com.andry.address.oriontek.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

public class Utils {

    /**
     * This method valid the status the Network
     *
     * @param context the context the app
     * @return Boolean
     */
    public static Boolean verifyNetwork(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null & cm.getActiveNetworkInfo().isAvailable()
                    & cm.getActiveNetworkInfo().isConnectedOrConnecting();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /***
     * getUidInSharedPreferences
     * @param context
     * @return
     */
    public static String getUidInSharedPreferences(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(Constants.nameFileTheSharedPreferences, Context.MODE_PRIVATE);

        return preferences.getString(Constants.uid, "");
    }

    /***
     * getUidInSharedPreferences
     * @param context
     * @return
     */
    public static String getRolInSharedPreferences(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(Constants.nameFileTheSharedPreferences, Context.MODE_PRIVATE);

        return preferences.getString(Constants.ROL, "");
    }
}
