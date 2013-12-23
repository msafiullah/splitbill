package com.msafiullah.splitbill.viewcontrollers.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.SplitBillApp;
import com.msafiullah.splitbill.util.FloatUtil;

public class BuyerItem {

	private static final int ITEM_LAYOUT = R.layout.buyer_item;

	private View _view;
	private TextView _buyerTextView;
	private TextView _amountPayableTextView;

	public BuyerItem(ViewGroup parent) {
		_view = LayoutInflater.from(parent.getContext()).inflate(
				ITEM_LAYOUT, parent, false);
		
		_buyerTextView = (TextView) _view.findViewById(R.id.tvBuyer);
		_amountPayableTextView = (TextView) _view.findViewById(R.id.tvTotal);
	}

	public View getView() {
		return _view;
	}
	
	public void setBuyer(String buyer) {
		_buyerTextView.setText(buyer);
	}

	public void setAmountPayable(float amountPayable) {
		_amountPayableTextView.setText(SplitBillApp.DollarSign
				+ FloatUtil.roundUpToMoneyStr(amountPayable));
	}

}
