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
    private Dialog dialogLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        checkKnoxSdkSupported();
        KnoxActivationManager.getInstance().register(this, activationCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister activator manager callback
        KnoxActivationManager.getInstance().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle activity result from Knox automatically
        KnoxActivationManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

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
        public void onKnoxLicenseActivated() {
            saveKnoxLicenseActivationStateToSharedPreference();
            if (KnoxActivationManager.getInstance().isLegacySdk()) {
                activateBackwardLicense();
            } else {
                setLicenseActivationSuccess();
            }
        }

        @Override
        public void onBackwardLicenseActivated() {
            saveBackwardLicenseActivationStateToSharedPreference();
            setLicenseActivationSuccess();
        }

        @Override
        public void onLicenseActivateFailed(int errorType, String errorMessage) {
            hideLoadingDialog();
            showLicenseActivationProblem(errorType, errorMessage);
        }
    };

    private void checkKnoxSdkSupported() {
        if (KnoxActivationManager.getInstance().isKnoxSdkSupported()) {
            activateDeviceAdmin();
        } else {
            showDeviceUnsupportedProblem();
        }
    }

    private void activateDeviceAdmin() {
        if (KnoxActivationManager.getInstance().isDeviceAdminActivated(this)) {
            setDeviceAdminActivated();
        } else {
            KnoxActivationManager.getInstance().activateDeviceAdmin(this, null);
        }
    }

    private void activateKnoxLicense() {
        showLoadingDialog();
        if (SharedPreferenceManager.isKnoxLicenseActivated(this)) {
            activationCallback.onKnoxLicenseActivated();
        } else {
            KnoxActivationManager.getInstance().activateKnoxLicense(this, LicenseKey.KNOX_LICENSE_KEY);
        }
    }

    private void activateBackwardLicense() {
        if (SharedPreferenceManager.isBackwardLicenseActivated(this)) {
            activationCallback.onBackwardLicenseActivated();
        } else {
            KnoxActivationManager.getInstance().activateBackwardLicense(this, LicenseKey.BACKWARD_LICENSE_KEY);
        }
    }

    private void setDeviceAdminActivated() {
        showDeviceAdminActivationSuccess();
        activateKnoxLicense();
    }

    private void setLicenseActivationSuccess() {
        hideLoadingDialog();
        showLicenseActivationSuccess();
        goToDoSomethingActivity();
    }

    private void saveKnoxLicenseActivationStateToSharedPreference() {
        SharedPreferenceManager.setKnoxLicenseActivated(this);
    }

    private void saveBackwardLicenseActivationStateToSharedPreference() {
        SharedPreferenceManager.setBackwardLicenseActivated(this);
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
