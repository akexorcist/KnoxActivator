package com.akexorcist.knoxactivator;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.akexorcist.knoxactivator.receiver.KnoxDeviceAdminReceiver;
import com.akexorcist.knoxactivator.receiver.KnoxLicenseReceiver;
import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.license.EnterpriseLicenseManager;
import com.samsung.android.knox.license.KnoxEnterpriseLicenseManager;

/**
 * Created by Akexorcist on 4/20/2016 AD.
 */

@SuppressWarnings("unused")
public class KnoxActivationManager {
    @SuppressWarnings("WeakerAccess")
    public static final int REQUEST_CODE_KNOX = 4545;

    private static KnoxActivationManager knoxActivationManager;

    public static KnoxActivationManager getInstance() {
        if (knoxActivationManager == null) {
            knoxActivationManager = new KnoxActivationManager();
        }
        return knoxActivationManager;
    }

    private ActivationCallback activationCallback;

    public void register(Context context, ActivationCallback callback) {
        this.activationCallback = callback;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EnterpriseLicenseManager.ACTION_LICENSE_STATUS);
        intentFilter.addAction(KnoxLicenseReceiver.ACTION_ELM_LICENSE_STATUS);
        intentFilter.addAction(KnoxLicenseReceiver.ACTION_KLM_LICENSE_STATUS);
        context.registerReceiver(knoxLicenseReceiver, intentFilter);
    }

    public void unregister(Context context) {
        activationCallback = null;
        context.unregisterReceiver(knoxLicenseReceiver);
    }

    @SuppressWarnings("unused")
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_KNOX) {
            if (activationCallback != null) {
                if (resultCode == Activity.RESULT_OK) {
                    activationCallback.onDeviceAdminActivated();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    activationCallback.onDeviceAdminActivationCancelled();
                }
            }
        }
    }

    public void activateDeviceAdmin(Activity activity, String description) {
        ComponentName componentName = new ComponentName(activity, KnoxDeviceAdminReceiver.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        if (description != null) {
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, description);
        }
        activity.startActivityForResult(intent, REQUEST_CODE_KNOX);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean deactivateDeviceAdmin(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (devicePolicyManager != null) {
            ComponentName componentName = new ComponentName(context, KnoxDeviceAdminReceiver.class);
            devicePolicyManager.removeActiveAdmin(componentName);
            return true;
        }
        return false;
    }

    public void activateKnoxLicense(Context context, String licenseKey) {
        KnoxEnterpriseLicenseManager knoxEnterpriseLicenseManager = KnoxEnterpriseLicenseManager.getInstance(context);
        knoxEnterpriseLicenseManager.activateLicense(licenseKey);
    }

    public void deactivateKnoxLicense(Context context, String licenseKey) {
        KnoxEnterpriseLicenseManager knoxEnterpriseLicenseManager = KnoxEnterpriseLicenseManager.getInstance(context);
        knoxEnterpriseLicenseManager.deActivateLicense(licenseKey);
    }

    public void activateBackwardLicense(Context context, String licenseKey) {
        EnterpriseLicenseManager enterpriseLicenseManager = EnterpriseLicenseManager.getInstance(context);
        enterpriseLicenseManager.activateLicense(licenseKey);
    }

    public boolean isDeviceAdminActivated(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, KnoxDeviceAdminReceiver.class);
        return devicePolicyManager != null && devicePolicyManager.isAdminActive(componentName);
    }

    public boolean isKnoxSdkSupported() {
        try {
            EnterpriseDeviceManager.getAPILevel();
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isMdmApiSupported(int requiredVersion) {
        try {
            return EnterpriseDeviceManager.getAPILevel() < requiredVersion;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLegacySdk() {
        try {
            return EnterpriseDeviceManager.getAPILevel() < EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_8;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

    private KnoxLicenseReceiver knoxLicenseReceiver = new KnoxLicenseReceiver() {
        @Override
        public void onKnoxLicenseActivated(Context context) {
            if (activationCallback != null) {
                activationCallback.onKnoxLicenseActivated();
            }
        }

        @Override
        public void onBackwardLicenseActivated(Context context) {
            if (activationCallback != null) {
                activationCallback.onBackwardLicenseActivated();
            }
        }

        @Override
        public void onLicenseActivationFailed(Context context, int errorCode) {
            if (activationCallback != null) {
                activationCallback.onLicenseActivateFailed(errorCode, getErrorMessage(errorCode));
            }
        }
    };

    private String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case EnterpriseLicenseManager.ERROR_INTERNAL:
                return "ERROR_INTERNAL";
            case EnterpriseLicenseManager.ERROR_INTERNAL_SERVER:
                return "ERROR_INTERNAL_SERVER";
            case EnterpriseLicenseManager.ERROR_INVALID_LICENSE:
                return "ERROR_INVALID_LICENSE";
            case EnterpriseLicenseManager.ERROR_INVALID_PACKAGE_NAME:
                return "ERROR_INVALID_PACKAGE_NAME";
            case EnterpriseLicenseManager.ERROR_LICENSE_TERMINATED:
                return "ERROR_LICENSE_TERMINATED";
            case EnterpriseLicenseManager.ERROR_NETWORK_DISCONNECTED:
                return "ERROR_NETWORK_DISCONNECTED";
            case EnterpriseLicenseManager.ERROR_NETWORK_GENERAL:
                return "ERROR_NETWORK_GENERAL";
            case EnterpriseLicenseManager.ERROR_NOT_CURRENT_DATE:
                return "ERROR_NOT_CURRENT_DATE";
            case EnterpriseLicenseManager.ERROR_NULL_PARAMS:
                return "ERROR_NULL_PARAMS";
            case EnterpriseLicenseManager.ERROR_SIGNATURE_MISMATCH:
                return "ERROR_SIGNATURE_MISMATCH";
            case EnterpriseLicenseManager.ERROR_USER_DISAGREES_LICENSE_AGREEMENT:
                return "ERROR_USER_DISAGREES_LICENSE_AGREEMENT";
            case EnterpriseLicenseManager.ERROR_VERSION_CODE_MISMATCH:
                return "ERROR_VERSION_CODE_MISMATCH";
            case EnterpriseLicenseManager.ERROR_UNKNOWN:
            default:
                return "ERROR_UNKNOWN";
        }
    }
}
