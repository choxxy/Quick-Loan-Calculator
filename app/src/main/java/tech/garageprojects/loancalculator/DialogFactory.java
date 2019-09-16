package tech.garageprojects.loancalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public final class DialogFactory {


    public static AlertDialog ExtraPayDialog(Context context, String content
            , DialogCallback dialogCallback) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.extra_payment__popup_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(dialogView);

        final TextView tvTitle = dialogView.findViewById(R.id.title);
        final TextView tvContent = dialogView.findViewById(R.id.content);

        // set dialog
        AlertDialog dialog = alertDialogBuilder.setCancelable(false).create();
        dialog.show();

       /* dismissButton.setOnClickListener(view -> {
            dialog.dismiss();
        });

        actionButton.setOnClickListener(view -> {
            dialogCallback.doPositiveClick();
            dialog.dismiss();
        });*/

        View v = dialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        return dialog;

    }


}