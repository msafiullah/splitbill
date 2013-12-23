package com.msafiullah.splitbill.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class NewBillConfirmationDialog extends AlertDialog {

	public NewBillConfirmationDialog(Context context,
			DialogInterface.OnClickListener positiveButtonListener) {
		super(context);

		setTitle("New Bill");
		setMessage("This will clear the existing bill data. Continue?");

		setButton(BUTTON_POSITIVE, "Yes", positiveButtonListener);
		setButton(BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				cancel();
			}
		});

		setCancelable(true);
	}

}
