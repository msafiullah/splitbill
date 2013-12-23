package com.msafiullah.splitbill.activity;

import android.os.Bundle;
import android.view.View;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.viewcontrollers.main.SplitViewCtrl;

public class SplitActivity extends AbstractActivity {

	// private final String tag = "SplitActivity";

	private SplitViewCtrl _splitViewCtrl;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View view = View.inflate(this, R.layout.split_activity, null);
		setContentView(view);

		_splitViewCtrl = new SplitViewCtrl(view);

	}

	protected void onResume() {
		super.onResume();
		// must refresh data on resume
		// because user might have changed content on other activities
		_splitViewCtrl.refreshListAndUpdateGrandTotal();
	}

	@Override
	protected void saveData() {
		// Nothing to save for this activity
	}

}
