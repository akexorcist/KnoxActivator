package com.akexorcist.knoxactivator.sample.manager;

import android.content.Context;

/**
 * Created by Akexorcist on 6/9/16 AD.
 */
public class SharedPreferenceManager {
    private static final String PREFERENCE_STATE = "preference_state";
    private static final String KEY_KNOX_LICENSE_ACTIVATED = "key_knox_license_activated";
    private static final String KEY_BACKWARD_LICENSE_ACTIVATED = "key_backward_license_activated";

    public static void setKnoxLicenseActivated(Context context) {
        context.getSharedPreferences(PREFERENCE_STATE, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_KNOX_LICENSE_ACTIVATED, true)
                .apply();
    }

    public static boolean isKnoxLicenseActivated(Context context) {
        return context.getSharedPreferences(PREFERENCE_STATE, Context.MODE_PRIVATE)
                .getBoolean(KEY_KNOX_LICENSE_ACTIVATED, false);
    }

    public static void setBackwardLicenseActivated(Context context) {
        context.getSharedPreferences(PREFERENCE_STATE, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_BACKWARD_LICENSE_ACTIVATED, true)
                .apply();
    }

    public static boolean isBackwardLicenseActivated(Context context) {
        return context.getSharedPreferences(PREFERENCE_STATE, Context.MODE_PRIVATE)
                .getBoolean(KEY_BACKWARD_LICENSE_ACTIVATED, false);
    }
}
