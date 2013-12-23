package com.msafiullah.splitbill.listeners;

import java.util.List;

import com.msafiullah.splitbill.viewcontrollers.item.BillItem;

public interface BillItemEventListener {
	
	public abstract void onPriceChanged(final BillItem item, final float priceChange);
	public abstract void onBuyerDeleted(final BillItem item, final String deletedBuyer);
	public abstract void onBuyersAdded(final BillItem item, final List<String> addedBuyers);
	public abstract void onBecomesEmpty(final BillItem item);
	public abstract void onContentChanged(final BillItem item);

	public abstract void onDelete(final BillItem item);
	public abstract void onDuplicate(final BillItem item);
	public abstract void onShowSelectBuyersDialogEvent(final BillItem item);
	
}
