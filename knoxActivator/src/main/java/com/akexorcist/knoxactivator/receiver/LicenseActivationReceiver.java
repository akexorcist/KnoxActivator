/**
 * DISCLAIMER: PLEASE TAKE NOTE THAT THE SAMPLE APPLICATION AND
 * SOURCE CODE DESCRIBED HEREIN IS PROVIDED FOR TESTING PURPOSES ONLY.
 * <p/>
 * Samsung expressly disclaims any and all warranties of any kind,
 * whether express or implied, including but not limited to the implied warranties and conditions
 * of merchantability, fitness for a particular purpose and non-infringement.
 * Further, Samsung does not represent or warrant that any portion of the sample application and
 * source code is free of inaccuracies, errors, bugs or interruptions, or is reliable,
 * accurate, complete, or otherwise valid. The sample application and source code is provided
 * "as is" and "as available", without any warranty of any kind from Samsung.
 * <p/>
 * Your use of the sample application and source code is at its own discretion and risk,
 * and licensee will be solely responsible for any damage that results from the use of the sample
 * application and source code including, but not limited to, any damage to your computer system or
 * platform. For the purpose of clarity, the sample code is licensed “as is” and
 * licenses bears the risk of using it.
 * <p/>
 * Samsung shall not be liable for any direct, indirect or consequential damages or
 * costs of any type arising out of any action taken by you or others related to the sample application
 * and source code.
 */

package com.akexorcist.knoxactivator.receiver;

import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.akexorcist.knoxactivator.KnoxActivationBus;
import com.akexorcist.knoxactivator.event.LicenseActivatedEvent;
import com.akexorcist.knoxactivator.event.LicenseActivationFailedEvent;

public class LicenseActivationReceiver extends BroadcastReceiver {
    private static final long EVENT_LISTENER_DELAY = 500;

    public LicenseActivationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && action.equals(EnterpriseLicenseManager.ACTION_LICENSE_STATUS)) {
                int errorCode = intent.getIntExtra(EnterpriseLicenseManager.EXTRA_LICENSE_ERROR_CODE, EnterpriseLicenseManager.ERROR_UNKNOWN);
                if (errorCode == EnterpriseLicenseManager.ERROR_NONE) {
                    onLicenseActivated();
                } else {
                    onLicenseActivationFailed(errorCode);
                }
            }
        }
    }

    private void onLicenseActivated() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KnoxActivationBus.getInstance().getBus().post(new LicenseActivatedEvent());
            }
        }, EVENT_LISTENER_DELAY);
    }

    private void onLicenseActivationFailed(final int errorCode) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KnoxActivationBus.getInstance().getBus().post(new LicenseActivationFailedEvent(errorCode));
            }
        }, EVENT_LISTENER_DELAY);
    }
}
