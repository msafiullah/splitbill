package com.msafiullah.splitbill.viewcontrollers.item;

import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.msafiullah.splitbill.SplitBillApp;
import com.msafiullah.splitbill.util.FloatUtil;

public class TotalAndSettlementDisplayItem extends TotalDisplayItem {

	private TextView _settlementTextView;

	public TotalAndSettlementDisplayItem(TextView tvTotal, TextView tvSettlement) {
		super(tvTotal);
		_settlementTextView = tvSettlement;
		reset();
	}

	public void reset() {
		super.reset();
		resetSettlementDisplay();
	}

	public void resetSettlementDisplay() {
		// must check for null because during the creation of this object, this
		// method will be called before following variable is initialized
		if (_settlementTextView != null) {
			_settlementTextView.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Updates settlement display with given amount.
	 * <p>
	 * <b>SIDE EFFECT</b>Changes the color of the display text based on the
	 * settlement balance.
	 * </p>
	 * 
	 * @param settlementAmount
	 */
	public void updateSettlementDisplay(float settlementAmount) {
		_settlementTextView.setText(Html
				.fromHtml(formatAmountForSettlementTextView(settlementAmount)));
		formatSettlementTextViewForBalance(settlementAmount - getTotal());
	}

	private void formatSettlementTextViewForBalance(float settlementBalance) {
		if (settlementBalance < 0) {// deficit
			_settlementTextView.setTextColor(Color.parseColor("#eb1313"));
			_settlementTextView.setVisibility(View.VISIBLE);

		} else if (settlementBalance > 0) {// excess
			_settlementTextView.setTextColor(Color.parseColor("#08d80d"));
			_settlementTextView.setVisibility(View.VISIBLE);
		} else {
			_settlementTextView.setVisibility(View.INVISIBLE);
		}
	}

	private String formatAmountForSettlementTextView(float settlementAmount) {
		final float settlementBalance = settlementAmount - getTotal();

		String balanceType = "";

		if (settlementBalance < 0) {// deficit
			balanceType = "Deficit";
		} else if (settlementBalance > 0) {// excess
			balanceType = "Excess";
		} else {
			return "";
		}

		return "Collect: " + SplitBillApp.DollarSign
				+ FloatUtil.roundUpToMoneyStr(settlementAmount) + "<br/>"
				+ balanceType + ": "
				+ FloatUtil.roundUpToMoneyStr(settlementBalance);
	}
}
