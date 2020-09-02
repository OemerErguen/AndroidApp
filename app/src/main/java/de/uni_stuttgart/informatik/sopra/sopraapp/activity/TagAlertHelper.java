package de.uni_stuttgart.informatik.sopra.sopraapp.activity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.persistence.CockpitDbHelper;
import de.uni_stuttgart.informatik.sopra.sopraapp.persistence.model.Tag;

/**
 * helper class to manage tag dialog
 */
public class TagAlertHelper {
    public static final String TAG = TagAlertHelper.class.getName();

    private Context context;
    private EditText tagNameEditText;
    private long currentTagId = 0;

    public TagAlertHelper(Context context) {
        this.context = context;
    }

    /**
     * method to show tag dialog for create and edit mode
     *
     * @param tagRecord
     * @param isEditMode
     */
    public void showTagEditDialog(@Nullable Tag tagRecord, boolean isEditMode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_tag, null, false);
        String dialogTitle;
        if (isEditMode) {
            dialogTitle = context.getString(R.string.dialog_tag_edit_title);
        } else {
            dialogTitle = context.getString(R.string.dialog_tag_add_title);
        }

        tagNameEditText = dialogView.findViewById(R.id.dialog_tag_name_field);
        if (tagRecord != null) {
            tagNameEditText.setText(tagRecord.getName());
            currentTagId = tagRecord.getId();
        }

        builder.setTitle(dialogTitle)
                .setCancelable(false)
                .setIcon(R.drawable.ic_receipt_black)
                .setView(dialogView)
                .setPositiveButton(R.string.btn_ok, null)
                .setNeutralButton(R.string.close, (dialog, which) -> {
                    dialog.cancel();
                });
        if (isEditMode) {
            builder.setNegativeButton(R.string.dialog_delete_tag_label, (dialog, which) -> {
                CockpitDbHelper dbHelper = new CockpitDbHelper(context);
                dbHelper.removeTag(tagRecord);
                if (context instanceof ProtectedActivity) {
                    ((ProtectedActivity) context).restartQueryCall();
                }
                dbHelper.close();
                dialog.cancel();
            });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
            handleForm(tagRecord, isEditMode, alertDialog);
        });
    }

    /**
     * method to handle user input
     *
     * @param tagRecord
     * @param isEditMode
     * @param alertDialog
     */
    private void handleForm(@Nullable Tag tagRecord, boolean isEditMode, AlertDialog alertDialog) {
        String tagName = tagNameEditText.getText().toString();

        CockpitDbHelper dbHelper = new CockpitDbHelper(context);

        if (tagName.length() >= 3) {
            Log.d(TAG, "user input valid");
            if (!isEditMode) {
                // insert
                dbHelper.addNewTag(dbHelper.getReadableDatabase(), tagName);
            } else {
                // update
                Tag updatedCustomQuery;
                if (tagRecord != null) {
                    updatedCustomQuery = new Tag(tagRecord.getId(), tagName);
                    dbHelper.updateTag(updatedCustomQuery);
                }
            }
            dbHelper.close();

            if (context instanceof ProtectedActivity) {
                ((ProtectedActivity) context).restartQueryCall();
            }

            alertDialog.cancel();
        } else {
            Log.d(TAG, "input not valid!");
            //Animation if pattern isn´t matching
            Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
            tagNameEditText.startAnimation(shake);
            //Text output
            tagNameEditText.setError(context.getString(R.string.invalid_user_input));
        }
    }

    public String getCurrentTagInput() {
        if (tagNameEditText != null) {
            return tagNameEditText.getText().toString();
        }
        return null;
    }

    public long getCurrentTagId() {
        return currentTagId;
    }
}