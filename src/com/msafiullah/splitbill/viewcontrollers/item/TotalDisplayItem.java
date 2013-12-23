package com.msafiullah.splitbill.viewcontrollers.item;

import android.widget.TextView;

import com.msafiullah.splitbill.SplitBillApp;
import com.msafiullah.splitbill.util.FloatUtil;

public class TotalDisplayItem {

	private float _total;
	private TextView _totalTextView;

	public TotalDisplayItem(TextView tvTotal) {
		_totalTextView = tvTotal;
		reset();
	}

	public float getTotal() {
		return _total;
	}

	public void addToTotal(float amount) {
		final float newAmt = _total + amount;
		if (newAmt > 0) {
			setTotal(newAmt);
		} else {
			reset();
		}
	}

	public void setTotal(float total) {
		_total = total;
		String string = FloatUtil.roundUpToMoneyStr(total);
		_totalTextView.setText(SplitBillApp.DollarSign + string);
	}

	public void reset() {
		_total = 0;
		_totalTextView.setText(SplitBillApp.DollarSign + "0.00");
	}

}
