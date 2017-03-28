package com.akexorcist.knoxactivator;

/**
 * Created by Akexorcist on 4/20/2016 AD.
 */
public interface ActivationCallback {
    void onDeviceAdminActivated();

    void onDeviceAdminActivationCancelled();

    void onDeviceAdminDeactivated();

    void onLicenseActivated();

    void onLicenseActivateFailed(int errorType, String errorMessage);
}
