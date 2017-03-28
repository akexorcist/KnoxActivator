package com.akexorcist.knoxactivator.event;

/**
 * Created by Akexorcist on 4/21/2016 AD.
 */
public class LicenseActivationFailedEvent {
    int errorType;

    public LicenseActivationFailedEvent(int errorType) {
        this.errorType = errorType;
    }

    public int getErrorType() {
        return errorType;
    }
}
