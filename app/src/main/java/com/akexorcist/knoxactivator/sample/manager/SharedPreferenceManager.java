package com.akexorcist.knoxactivator.sample.manager;

import android.content.Context;

/**
 * Created by Akexorcist on 6/9/16 AD.
 */
public class SharedPreferenceManager {
    private static final String PREFERENCE_STATE = "preference_state";
    private static final String KEY_LICENSE_ACTIVATED = "key_license_activated";

    public static void setLicenseActivated(Context context) {
        context.getSharedPreferences(PREFERENCE_STATE, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_LICENSE_ACTIVATED, true)
                .apply();
    }

    public static boolean isLicenseActivated(Context context) {
        return context.getSharedPreferences(PREFERENCE_STATE, Context.MODE_PRIVATE)
                .getBoolean(KEY_LICENSE_ACTIVATED, false);
    }
}
