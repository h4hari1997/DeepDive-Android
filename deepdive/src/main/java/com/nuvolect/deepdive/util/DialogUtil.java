/*
 * Copyright (c) 2018 Nuvolect LLC.
 * This software is offered for free under conditions of the GPLv3 open source software license.
 * Contact Nuvolect LLC for a less restrictive commercial license if you would like to use the software
 * without the GPLv3 restrictions.
 */

package com.nuvolect.deepdive.util;//

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.nuvolect.deepdive.main.CConst;

public class DialogUtil {

    private static AlertDialog confirmDialog_alert;
    private static AlertDialog dismissDialog_alert;
    private static AlertDialog dismissDialogMl_alert;
    private static AlertDialog oneButtonDialog_alert;
    private static AlertDialog twoButtonDialog_alert;

    public static interface DialogCallback {

        public void confirmed();
        public void canceled();
    }

    /**
     * Dialog with title, message and two buttons.
     * @param act
     * @param title
     * @param message
     * @param cancelButtonText
     * @param confirmButtonText
     * @param dialogCallback
     */
    public static void confirmDialog(
            Activity act,
            String title,
            String message,
            String cancelButtonText,
            String confirmButtonText,
            final DialogCallback dialogCallback) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(act);
        alert.setTitle(title);

//        alert.setIcon(R.drawable.mga_icon_96_outline);
        alert.setMessage(message);

        alert.setPositiveButton( confirmButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialogCallback.confirmed();
            }
        });

        alert.setNegativeButton( cancelButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                alert.setOnCancelListener( null);
                dialogCallback.canceled();
            }
        });

        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                dialogCallback.canceled();
            }
        });

        alert.show();
    }

    /**
     * Dialog with title and message with one button.
     * @param act
     * @param title
     * @param message
     * @param confirmButtonText
     * @param dialogCallback
     */
    public static void confirmDialog(
            Activity act,
            String title, String message,
            String confirmButtonText,
            final DialogCallback dialogCallback) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(act);
        alert.setTitle(title);

        alert.setMessage(message);

        alert.setPositiveButton(confirmButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialogCallback.confirmed();
            }
        });

        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                dialogCallback.canceled();
            }
        });

        alert.show();
    }

    public interface InputDialogCallbacks{

        public void done(String result);
        public void cancel();
    }

    public static void inputDialog(
            Activity act, String title, String message, String hint,
            boolean enableOkButton, final InputDialogCallbacks callbacks){

        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle(title);
        builder.setMessage(message);

        final EditText edittext = new EditText(act);
        edittext.setHint(hint);

        if( ! enableOkButton){
            edittext.setEnabled( false);
            edittext.setFocusable( false);
        }
        builder.setView(edittext);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String result = edittext.getText().toString();
                callbacks.done(result);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                callbacks.cancel();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                callbacks.cancel();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();

        //enable/ disable buttons of dialog
        if(! enableOkButton )
            dialog.getButton(AlertDialog.BUTTON1).setEnabled(false); //BUTTON1 is positive button
    }
    /** Simple dialog with a title, message and dismiss button, supports HTML */
    public static void dismissDialog(Activity act, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message));
        builder.setIcon(CConst.SMALL_ICON);
        builder.setCancelable(true);

        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dismissDialog_alert.cancel();
            }
        });
        dismissDialog_alert = builder.create();
        dismissDialog_alert.show();

        // Activate the HTML
        TextView tv = ((TextView) dismissDialog_alert.findViewById(android.R.id.message));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
    /** Simple multi-line dialog with a title, message and dismiss button */
    public static void dismissMlDialog(Activity act, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(act);

        TextView tw =new TextView(act);
        tw.setMaxLines(10);
        tw.setPadding( 13, 13, 13, 13);
        tw.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tw.setSingleLine(false);
        tw.setText(message);
        builder.setView(tw);

        builder.setTitle(title);
        builder.setIcon(CConst.SMALL_ICON);
        builder.setCancelable(true);

        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dismissDialogMl_alert.cancel();
            }
        });
        dismissDialogMl_alert = builder.create();
        dismissDialogMl_alert.show();
    }
    /** Multi-line dialog with a title, message, dismiss button and callback*/
    public static void oneButtonMlDialog(
            Activity act, String title, String message,
            String buttonTitle, final DialogUtilCallbacks callbacks) {

        AlertDialog.Builder builder = new AlertDialog.Builder(act);

        TextView tv =new TextView(act);
        tv.setMaxLines(10);
        tv.setPadding( 13, 13, 13, 13);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tv.setSingleLine(false);
        tv.setText(Html.fromHtml( message));
        builder.setView(tv);

        builder.setTitle(title);
        builder.setIcon(CConst.SMALL_ICON);
        builder.setCancelable(true);

        builder.setNegativeButton( buttonTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                oneButtonDialog_alert.cancel();
                callbacks.confirmed( true);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                callbacks.confirmed( false);
            }
        });
        oneButtonDialog_alert = builder.create();
        oneButtonDialog_alert.show();

        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
    /** Multi-line dialog with a title, message, dismiss button and second button*/
    public static void twoButtonMlDialog(
            Activity act, String title, String message,
            String negativeButtonTitle, String positiveButtonTitle,
            final DialogUtilCallbacks callbacks) {

        AlertDialog.Builder builder = new AlertDialog.Builder(act);

        TextView tw =new TextView(act);
        tw.setMaxLines(10);
        tw.setPadding( 13, 13, 13, 13);
        tw.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tw.setSingleLine(false);
        tw.setText(message);
        builder.setView(tw);

        builder.setTitle(title);
        builder.setIcon(CConst.SMALL_ICON);
        builder.setCancelable(true);

        builder.setPositiveButton( positiveButtonTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                twoButtonDialog_alert.cancel();
                callbacks.confirmed( true);
            }
        });
        builder.setNegativeButton( negativeButtonTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                twoButtonDialog_alert.cancel();
                callbacks.confirmed( false);
            }
        });
        builder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        twoButtonDialog_alert.cancel();
                        callbacks.confirmed( false);
                    }
                }
        );
        twoButtonDialog_alert = builder.create();
        twoButtonDialog_alert.show();
    }
    public interface DialogUtilCallbacks {

        public void confirmed( boolean confirmed);
    }

    /** Simple confirmation dialog with a title, message and OK and cancel button */
    public static void confirmDialog(
            Activity act, String title, String message, final DialogUtilCallbacks callbacks) {

        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(true);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                confirmDialog_alert.cancel();
                callbacks.confirmed( true);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                confirmDialog_alert.cancel();
                callbacks.confirmed( false);
            }
        });
        confirmDialog_alert = builder.create();
        confirmDialog_alert.show();

        // Activate the HTML
        TextView tv = ((TextView) confirmDialog_alert.findViewById(android.R.id.message));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
