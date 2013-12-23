package com.msafiullah.splitbill.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MenuDialog {

	final CharSequence[] MENU_ITEMS = { "New Bill", "Favourite Names", "Tax Option",
			"About", "Feedback / Rate" };
	
	private AlertDialog _menuDialog;

	public MenuDialog(Context context,
			DialogInterface.OnClickListener menuSelectionListener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setItems(MENU_ITEMS, menuSelectionListener);

		_menuDialog = builder.create();
		_menuDialog.setCancelable(true);
		_menuDialog.setCanceledOnTouchOutside(true);

	}

	public void show() {
		_menuDialog.show();
	}


}
