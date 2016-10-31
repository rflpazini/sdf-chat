package com.rflpazini.sdf.utils;

import android.content.Context;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.rflpazini.sdf.R;

/**
 * Created by rflpazini on 10/30/16.
 */

public class Dialogs {

    public void errorMessage(Context context, String msg) {
        new MaterialStyledDialog.Builder(context)
                .setIcon(R.drawable.ic_error_white_24dp)
                .withIconAnimation(true)
                .setDescription(msg)
                .setHeaderColor(R.color.colorAccent)
                .setStyle(Style.HEADER_WITH_ICON)
                .setPositive("Ok", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
