package com.akexorcist.knoxactivator.sample;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.akexorcist.knoxactivator.ActivationCallback;
import com.akexorcist.knoxactivator.KnoxActivationManager;
import com.akexorcist.knoxactivator.sample.manager.DialogManager;
import com.akexorcist.knoxactivator.sample.manager.SharedPreferenceManager;
import com.akexorcist.knoxactivator.sample.manager.ToastManager;

public class ActivationActivity extends AppCompatActivity {
    // TODO Put your ELM key here
    private final String LICENSE_KEY = "YOUR_ELM_KEY";

    private Dialog dialogLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        checkKnoxSdkSupported();
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO Register Knox activation manager with existing callback (at line 49)

    }

    @Override
    public void onStop() {
        super.onStop();
        // TODO Unregister Knox activation manager

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO Handle activity result from Knox activation manager

    }

    // Knox activation manager callback
    private ActivationCallback activationCallback = new ActivationCallback() {
        @Override
        public void onDeviceAdminActivated() {
            setDeviceAdminActivated();
        }

        @Override
        public void onDeviceAdminActivationCancelled() {
            showDeviceAdminActivationProblem();
        }

        @Override
        public void onDeviceAdminDeactivated() {

        }

        @Override
        public void onLicenseActivated() {
            hideLoadingDialog();
            saveLicenseActivationStateToSharedPreference();
            showLicenseActivationSuccess();
            goToDoSomethingActivity();
        }

        @Override
        public void onLicenseActivateFailed(int errorType, String errorMessage) {
            hideLoadingDialog();
            showLicenseActivationProblem(errorType, errorMessage);
        }
    };

    private void checkKnoxSdkSupported() {
        // TODO Fix below line, call Knox activation manager method to check the Knox sdk supporting
        boolean isKnoxSdkSupported = false;
        if (isKnoxSdkSupported) {
            activateDeviceAdmin();
        } else {
            showDeviceUnsupportedProblem();
        }
    }

    private void activateDeviceAdmin() {
        // TODO Fix below line, call Knox activation manager method to check the device administration activation
        boolean isDeviceAdminActivated = false;
        if (isDeviceAdminActivated) {
            setDeviceAdminActivated();
        } else {
            KnoxActivationManager.getInstance().activateDeviceAdmin(this);
        }
    }

    private void activateKnoxLicense() {
        // Restore license activation state from shared preference (Bypass)
        if (SharedPreferenceManager.isLicenseActivated(this)) {
            showLicenseActivationSuccess();
            goToDoSomethingActivity();
        } else {
            showLoadingDialog();
            KnoxActivationManager.getInstance().activateLicense(this, LICENSE_KEY);
        }
    }

    private void setDeviceAdminActivated() {
        showDeviceAdminActivationSuccess();
        activateKnoxLicense();
    }

    private void saveLicenseActivationStateToSharedPreference() {
        SharedPreferenceManager.setLicenseActivated(this);
    }

    private void goToDoSomethingActivity() {
        startActivity(new Intent(this, DoSomethingActivity.class));
        finish();
    }

    private void showLicenseActivationSuccess() {
        ToastManager.showLicenseActivationSuccess(this);
    }

    private void showDeviceAdminActivationSuccess() {
        ToastManager.showDeviceAdminActivationSuccess(this);
    }

    private void showDeviceUnsupportedProblem() {
        DialogManager.showDeviceUnsupportedProblem(this, new DialogManager.OnDialogClickAdapter() {
            @Override
            public void onNeutralClick() {
                finish();
            }
        });
    }

    private void showDeviceAdminActivationProblem() {
        DialogManager.showDeviceAdminActivationProblem(this, new DialogManager.OnDialogClickAdapter() {
            @Override
            public void onPositiveClick() {
                activateDeviceAdmin();
            }

            @Override
            public void onNegativeClick() {
                finish();
            }
        });
    }

    private void showLicenseActivationProblem(int errorType, String errorMessage) {
        DialogManager.showLicenseActivationProblem(this, errorType, errorMessage, new DialogManager.OnDialogClickAdapter() {
            @Override
            public void onPositiveClick() {
                activateKnoxLicense();
            }

            @Override
            public void onNegativeClick() {
                finish();
            }
        });
    }

    private void showLoadingDialog() {
        dialogLoading = DialogManager.showLicenseActivationLoading(this);
    }

    private void hideLoadingDialog() {
        if (dialogLoading != null) {
            dialogLoading.dismiss();
            dialogLoading = null;
        }
    }
}
