package com.akexorcist.knoxactivator.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.akexorcist.knoxactivator.BuildConfig;
import com.samsung.android.knox.license.EnterpriseLicenseManager;

public abstract class KnoxLicenseReceiver extends BroadcastReceiver {
    private static final String TAG = KnoxLicenseReceiver.class.getSimpleName();
    public static final int ERROR_DEFAULT = -1;
    public static final String ACTION_ELM_LICENSE_STATUS = "edm.intent.action.license.status";
    public static final String ACTION_KLM_LICENSE_STATUS = "edm.intent.action.knox_license.status";
    public static final String EXTRA_KLM_ERROR_CODE = "edm.intent.extra.knox_license.errorcode";
    public static final String EXTRA_ELM_ERROR_CODE = "edm.intent.extra.license.errorcode";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            debugLicenseStatus(intent);
            String action = intent.getAction();
            if (EnterpriseLicenseManager.ACTION_LICENSE_STATUS.equalsIgnoreCase(action)) {
                checkKnoxResultCode(context, intent);
                return;
            } else if (ACTION_KLM_LICENSE_STATUS.equalsIgnoreCase(action)) {
                checkKlmResultCode(context, intent);
                return;
            } else if (ACTION_ELM_LICENSE_STATUS.equalsIgnoreCase(action)) {
                checkElmResultCode(context, intent);
                return;
            }
        }
        onLicenseActivationFailed(context, EnterpriseLicenseManager.ERROR_UNKNOWN);
    }

    private void checkKnoxResultCode(Context context, Intent intent) {
        int errorCode = intent.getIntExtra(EnterpriseLicenseManager.EXTRA_LICENSE_ERROR_CODE, ERROR_DEFAULT);
        if (errorCode == EnterpriseLicenseManager.ERROR_NONE) {
            onKnoxLicenseActivated(context);
        } else {
            onLicenseActivationFailed(context, errorCode);
        }
    }

    private void checkKlmResultCode(Context context, Intent intent) {
        int errorCode = intent.getIntExtra(EXTRA_KLM_ERROR_CODE, ERROR_DEFAULT);
        if (errorCode == EnterpriseLicenseManager.ERROR_NONE) {
            onKnoxLicenseActivated(context);
        } else {
            onLicenseActivationFailed(context, errorCode);
        }
    }

    private void checkElmResultCode(Context context, Intent intent) {
        int errorCode = intent.getIntExtra(EXTRA_ELM_ERROR_CODE, ERROR_DEFAULT);
        if (errorCode == EnterpriseLicenseManager.ERROR_NONE) {
            onBackwardLicenseActivated(context);
        } else {
            onLicenseActivationFailed(context, errorCode);
        }
    }

    private void debugLicenseStatus(Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("Action : %s", intent.getAction()));
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Object value = bundle.get(key);
                    Log.d(TAG, String.format("%s %s (%s)", key,
                            value != null ? value.toString() : "null",
                            value != null ? value.getClass().getName() : ""));
                }
            }
        }
    }

    public abstract void onKnoxLicenseActivated(Context context);

    public abstract void onBackwardLicenseActivated(Context context);

    public abstract void onLicenseActivationFailed(Context context, int errorCode);
}
