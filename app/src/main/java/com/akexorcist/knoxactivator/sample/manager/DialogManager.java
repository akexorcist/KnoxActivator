package com.akexorcist.knoxactivator.sample.manager;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.akexorcist.knoxactivator.sample.R;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

/**
 * Created by Akexorcist on 4/22/2016 AD.
 */
public class DialogManager {
    public static Dialog showLicenseActivationLoading(Context context) {
        return new LovelyProgressDialog(context)
                .setTitle(R.string.license_activation_loading_title)
                .setMessage(R.string.license_activation_loading_content)
                .setTopColorRes(R.color.primary)
                .show();
    }

    public static void showDialog(Context context, String title, String content, String positive, String negative, final OnDialogClickListener listener) {
        new LovelyStandardDialog(context)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(positive, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onPositiveClick();
                        }
                    }
                })
                .setNegativeButton(negative, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onNegativeClick();
                        }
                    }
                })
                .setNegativeButtonColorRes(R.color.gray)
                .setPositiveButtonColorRes(R.color.accent)
                .setTopColorRes(R.color.primary)
                .show();
    }

    public static void showDeviceUnsupportedProblem(Context context, final OnDialogClickListener listener) {
        new LovelyStandardDialog(context)
                .setTitle(R.string.device_unsupported_title)
                .setMessage(R.string.device_unsupported_content)
                .setNeutralButton(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onNeutralClick();
                        }
                    }
                })
                .setNeutralButtonColorRes(R.color.accent)
                .setTopColorRes(R.color.primary)
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
