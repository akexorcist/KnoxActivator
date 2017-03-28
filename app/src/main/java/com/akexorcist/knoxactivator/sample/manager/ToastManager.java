package com.akexorcist.knoxactivator.sample.manager;

import android.content.Context;
import android.widget.Toast;

import com.akexorcist.knoxactivator.sample.R;


/**
 * Created by Akexorcist on 4/22/2016 AD.
 */
public class ToastManager {
    public static void showLicenseActivationSuccess(Context context) {
        showToast(context, R.string.license_activation_success);
    }

    public static void showDeviceAdminActivationSuccess(Context context) {
        showToast(context, R.string.device_admin_activation_success);
    }

    public static void showToast(Context context, int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
