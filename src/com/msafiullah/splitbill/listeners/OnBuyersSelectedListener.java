package com.msafiullah.splitbill.listeners;

import java.util.List;

import com.msafiullah.splitbill.viewcontrollers.item.BillItem;

public interface OnBuyersSelectedListener {

	public abstract void onBuyerSelectionCompleted(BillItem item, List<String> selectedNames);
	
}
