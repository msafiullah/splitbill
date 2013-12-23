package com.msafiullah.splitbill.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.msafiullah.splitbill.dialog.AboutDialog;
import com.msafiullah.splitbill.dialog.FavNamesDialog;
import com.msafiullah.splitbill.dialog.MenuDialog;
import com.msafiullah.splitbill.dialog.NewBillConfirmationDialog;
import com.msafiullah.splitbill.dialog.TaxOptionDialog;

public abstract class AbstractActivity extends Activity {

	private FavNamesDialog favNamesDialog;
	private TaxOptionDialog taxOptionDialog;
	private NewBillConfirmationDialog newBillConfirmationDialog;

	private void showMenuDialog() {
		final Context context = this;

		new MenuDialog(this, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					showNewBillConfirmationDialog();
					dialog.dismiss();
					break;
				case 1:
					showFavNamesDialog();
					dialog.dismiss();
					break;
				case 2:
					showTaxOptionDialog();
					dialog.dismiss();
					break;
				case 3:
					new AboutDialog(context).show();
					dialog.dismiss();
					break;
				case 4:
					sendIntentForGooglePlayStore();
					dialog.dismiss();
					break;
				}
			}
		}).show();
	}

	protected abstract void saveData();

	@Override
	protected void onPause() {
		Log.d("AbstractActivity", "onPause()");

		super.onPause();

		saveData();
	}

	private void showFavNamesDialog() {
		if (favNamesDialog == null) {
			favNamesDialog = new FavNamesDialog(this);
		}
		favNamesDialog.show();
	}

	private void showTaxOptionDialog() {
		if (taxOptionDialog == null) {
			taxOptionDialog = new TaxOptionDialog(this);
		}
		taxOptionDialog.show();
	}

	private void showNewBillConfirmationDialog() {
		if (newBillConfirmationDialog == null) {
			newBillConfirmationDialog = new NewBillConfirmationDialog(this,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							sendIntentForNewBill();
						}
					});
		}
		newBillConfirmationDialog.show();
	}

	private void sendIntentForGooglePlayStore() {
		String packageName = "com.msafiullah.splitbill";
		Uri marketUri = Uri.parse("market://details?id=" + packageName);
		Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
		startActivity(marketIntent);
	}

	private void sendIntentForNewBill() {
		Intent intent;
		try {
			intent = new Intent(this, getActivityClassFromTag("bill"));
			intent.putExtra("shouldUseNewBill", true);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is called when an action bar button is clicked. The
	 * onClick="" attribute for the action bar button is set to this method in
	 * the XML layout.
	 * 
	 * @param view
	 *            - the view that was clicked
	 */
	public final void action_btn_clicked(View view) {
		String tag = view.getTag().toString().trim();

		if (tag.equalsIgnoreCase("newbill")) {
			showNewBillConfirmationDialog();
		} else if (tag.equalsIgnoreCase("menu")) {
			showMenuDialog();
		} else {
			try {
				@SuppressWarnings("rawtypes")
				Class cls = getActivityClassFromTag(tag);
				if (!cls.equals(this.getClass())) {
					Intent intent = new Intent(this, cls);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("rawtypes")
	private static Class getActivityClassFromTag(String tag)
			throws ClassNotFoundException {
		if (tag.equalsIgnoreCase("bill")) {
			return BillActivity.class;
		} else if (tag.equalsIgnoreCase("charges")) {
			return ChargeActivity.class;
		} else if (tag.equalsIgnoreCase("split")) {
			return SplitActivity.class;
		} else {
			throw new ClassNotFoundException("Class for tag " + tag
					+ " cannot be found.");
		}
	}

}
