package com.msafiullah.splitbill.listeners;

import com.msafiullah.splitbill.viewcontrollers.item.ChargeItem;

public interface OnChargeItemContentChangedListener extends
		OnDecimalFieldItemContentChanged {
	public abstract void onChargeTypeChanged(ChargeItem item);
}
