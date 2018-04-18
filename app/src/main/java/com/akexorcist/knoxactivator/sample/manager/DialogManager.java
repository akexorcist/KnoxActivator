package com.akexorcist.knoxactivator.sample.manager;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.akexorcist.knoxactivator.sample.R;

/**
 * Created by Akexorcist on 4/22/2016 AD.
 */
public class DialogManager {
    public static Dialog showLicenseActivationLoading(Context context) {
        return new MaterialDialog.Builder(context)
                .title(R.string.license_activation_loading_title)
                .content(R.string.license_activation_loading_content)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    public static void showDialog(Context context, String title, String content, String positive, String negative, final OnDialogClickListener listener) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positive)
                .negativeText(negative)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) {
                            listener.onPositiveClick();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) {
                            listener.onNegativeClick();
                        }
                    }
                })
                .show();
    }

    public static void showDeviceUnsupportedProblem(Context context, final OnDialogClickListener listener) {
        new MaterialDialog.Builder(context)
                .title(R.string.device_unsupported_title)
                .content(R.string.device_unsupported_content)
                .neutralText(R.string.ok)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) {
                            listener.onNeutralClick();
                        }
                    }
                })
                .show();
    }

    public static void showDeviceAdminActivationProblem(Context context, OnDialogClickListener listener) {
        showDialog(context,
                context.getString(R.string.device_admin_cancelled_title),
                context.getString(R.string.device_admin_cancelled_content),
                context.getString(R.string.retry),
                context.getString(R.string.cancel),
                listener);
    }

    public static void showLicenseActivationProblem(Context context, int errorType, String errorMessage, OnDialogClickListener listener) {
        showDialog(context,
                context.getString(R.string.license_failed_title),
                context.getString(R.string.license_failed_content, errorMessage, errorType),
                context.getString(R.string.retry),
                context.getString(R.string.cancel),
                listener);
    }

    public interface OnDialogClickListener {
        void onPositiveClick();

        void onNegativeClick();

        void onNeutralClick();
    }

    public static class OnDialogClickAdapter implements OnDialogClickListener {
        @Override
        public void onPositiveClick() { /*....*/ }

        @Override
        public void onNegativeClick() { /*....*/ }

        @Override
        public void onNeutralClick() { /*....*/ }
    }
}
