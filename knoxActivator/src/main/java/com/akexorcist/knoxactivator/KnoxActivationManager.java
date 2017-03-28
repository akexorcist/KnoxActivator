package com.akexorcist.knoxactivator;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.akexorcist.knoxactivator.event.AdminDeactivatedEvent;
import com.akexorcist.knoxactivator.event.LicenseActivatedEvent;
import com.akexorcist.knoxactivator.event.LicenseActivationFailedEvent;
import com.akexorcist.knoxactivator.receiver.AdminActivationReceiver;
import com.squareup.otto.Subscribe;

/**
 * Created by Akexorcist on 4/20/2016 AD.
 */
public class KnoxActivationManager {
    private static final long EVENT_LISTENER_DELAY = 500;
    public static final int REQUEST_CODE_KNOX = 4545;

    private static KnoxActivationManager knoxActivationManager;

    public static KnoxActivationManager getInstance() {
        if (knoxActivationManager == null) {
            knoxActivationManager = new KnoxActivationManager();
        }
        return knoxActivationManager;
    }

    private ActivationCallback activationCallback;

    public void register(ActivationCallback callback) {
        activationCallback = callback;
        KnoxActivationBus.getInstance().getBus().register(this);
    }

    public void unregister() {
        activationCallback = null;
        KnoxActivationBus.getInstance().getBus().unregister(this);
    }

    public boolean isDeviceAdminActivated(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, AdminActivationReceiver.class);
        return devicePolicyManager.isAdminActive(componentName);
    }

    @Subscribe
    public void onDeviceAdminDeactivated(AdminDeactivatedEvent event) {
        if (activationCallback != null) {
            activationCallback.onDeviceAdminDeactivated();
        }
    }

    @Subscribe
    public void onLicenseActivated(LicenseActivatedEvent event) {
        if (activationCallback != null) {
            activationCallback.onLicenseActivated();
        }
    }

    @Subscribe
    public void onLicenseActivationFailed(LicenseActivationFailedEvent event) {
        if (activationCallback != null) {
            int errorType = event.getErrorType();
            activationCallback.onLicenseActivateFailed(errorType, getErrorMessage(errorType));
        }
    }

    @SuppressWarnings({"WrongConstant", "TryWithIdenticalCatches"})
    public boolean isKnoxSdkSupported(Context context) {
        try {
            EnterpriseDeviceManager enterpriseDeviceManager = (EnterpriseDeviceManager) context.getSystemService(EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);
            enterpriseDeviceManager.getEnterpriseSdkVer();
            return true;
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("WrongConstant")
    public boolean isMdmApiSupported(Context context, EnterpriseDeviceManager.EnterpriseSdkVersion requiredVersion) {
        EnterpriseDeviceManager enterpriseDeviceManager = (EnterpriseDeviceManager) context.getSystemService(EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);
        return !(requiredVersion != null && enterpriseDeviceManager.getEnterpriseSdkVer().ordinal() < requiredVersion.ordinal());
    }

    public void activateLicense(Context context, String licenseKey) {
        EnterpriseLicenseManager enterpriseLicenseManager = EnterpriseLicenseManager.getInstance(context);
        enterpriseLicenseManager.activateLicense(licenseKey, context.getPackageName());
    }

    public void activateDeviceAdmin(Activity activity) {
        ComponentName componentName = new ComponentName(activity, AdminActivationReceiver.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        activity.startActivityForResult(intent, REQUEST_CODE_KNOX);
    }

    public void deactivateDeviceAdmin(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, AdminActivationReceiver.class);
        devicePolicyManager.removeActiveAdmin(componentName);
    }

    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_KNOX) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (activationCallback != null) {
                        if (resultCode == Activity.RESULT_OK) {
                            activationCallback.onDeviceAdminActivated();
                        } else if (resultCode == Activity.RESULT_CANCELED) {
                            activationCallback.onDeviceAdminActivationCancelled();
                        }
                    }
                }
            }, EVENT_LISTENER_DELAY);
        }
    }

    public String getErrorMessage(int errorType) {
        switch (errorType) {
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
            case EnterpriseLicenseManager.ERROR_NO_MORE_REGISTRATION:
                return "ERROR_NO_MORE_REGISTRATION";
            case EnterpriseLicenseManager.ERROR_NOT_CURRENT_DATE:
                return "ERROR_NOT_CURRENT_DATE";
            case EnterpriseLicenseManager.ERROR_NULL_PARAMS:
                return "ERROR_NULL_PARAMS";
            case EnterpriseLicenseManager.ERROR_USER_DISAGREES_LICENSE_AGREEMENT:
                return "ERROR_USER_DISAGREES_LICENSE_AGREEMENT";
            case EnterpriseLicenseManager.ERROR_UNKNOWN:
            default:
                return "ERROR_UNKNOWN";
        }
    }
}
