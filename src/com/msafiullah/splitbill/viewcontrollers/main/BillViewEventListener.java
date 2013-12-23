package com.msafiullah.splitbill.viewcontrollers.main;

import com.msafiullah.splitbill.viewcontrollers.item.BillItem;

public interface BillViewEventListener {
	
	public abstract void onScrollViewEndReached();
	
	public abstract void onBeforeBillViewFocusLost();
	
	public abstract void onEntryItemIsUsedUp(BillItem item);
	
}
