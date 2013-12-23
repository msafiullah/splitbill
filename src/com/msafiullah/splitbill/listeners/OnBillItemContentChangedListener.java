package com.msafiullah.splitbill.listeners;

import java.util.List;

import com.msafiullah.splitbill.viewcontrollers.item.BillItem;

public interface OnBillItemContentChangedListener {
	
	public abstract void onContentChanged(final BillItem item);
	public abstract void onPriceChanged(final BillItem item, final float priceChange);
	public abstract void onBuyerDeleted(final BillItem item, final String deletedBuyer);
	public abstract void onBuyersAdded(final BillItem item, final List<String> addedBuyers);
	
}
