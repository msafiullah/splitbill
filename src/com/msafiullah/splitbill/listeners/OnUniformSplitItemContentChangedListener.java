package com.msafiullah.splitbill.listeners;

import com.msafiullah.splitbill.viewcontrollers.item.UniformSplitItem;

public interface OnUniformSplitItemContentChangedListener extends
		OnDecimalFieldItemContentChanged {
	public abstract void onNumPersonChanged(UniformSplitItem item);
}
