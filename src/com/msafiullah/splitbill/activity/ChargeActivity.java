package com.msafiullah.splitbill.activity;

import android.os.Bundle;
import android.view.View;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.viewcontrollers.main.ChargeViewCtrl;

public class ChargeActivity extends AbstractActivity {

	// private final String tag = "ChargeActivity";

	private ChargeViewCtrl _chargeView = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View view = View.inflate(this, R.layout.charge_activity, null);
		setContentView(view);

		_chargeView = new ChargeViewCtrl(view);
	}

	protected void onResume() {
		super.onResume();
		// must load data on resume
		// because user might have inputed some new content in Bill Activity
		_chargeView.loadData();
	}

	protected void saveData() {
		// save data to preference
		_chargeView.saveData();
	}

}