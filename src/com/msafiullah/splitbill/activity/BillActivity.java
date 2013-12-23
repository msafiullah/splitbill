package com.msafiullah.splitbill.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.viewcontrollers.main.BillViewCtrl;

public class BillActivity extends AbstractActivity {

	private final String tag = "BillActivity";

	private BillViewCtrl _billViewCtrl = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.bill_activity, null);
		setContentView(view);

		Log.d(tag, "onCreate(..) -> new BillViewCtrl(view)");
		_billViewCtrl = new BillViewCtrl(view);

		if (haveReceivedNewBillIntent()) {
			_billViewCtrl.resetBill();
		}

		_billViewCtrl
				.retrieveBillItemCountAndTotalFromTempRecordAsynchronously();

	}

	protected void saveData() {
		// i know it's a weird place to call the on before bill view focus lost
		// method below.
		// it is supposed to be called when the bill activity is going to go
		// into the background (i.e. onPause())
		// but since the saveData() method is ONLY invoked by the
		// super.onPause() method in AbstractActivity, we can safely put the on
		// before bill view focus lost method here.
		_billViewCtrl.onBeforeBillViewFocusLost();

		_billViewCtrl.saveBillTotal();

	}

	private boolean haveReceivedNewBillIntent() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			return extras.getBoolean("shouldUseNewBill", false);
		}
		return false;
	}

}