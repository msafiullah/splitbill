package com.msafiullah.splitbill.listeners;

import com.msafiullah.splitbill.viewcontrollers.item.AbstractDecimalFieldItem;

public interface OnDecimalFieldItemContentChanged {

	public abstract void onDecimalAmountChanged(final AbstractDecimalFieldItem item);

}
