package com.msafiullah.splitbill.listeners;

import com.msafiullah.splitbill.viewcontrollers.item.BillItem;

public interface OnBillItemActionPerformedListener {
	
	public abstract void onDuplicateButtonClicked(BillItem item);
	public abstract void onDeleteButtonClicked(BillItem item);
	public abstract void onShowSelectBuyersDialog(BillItem item);
	
}
