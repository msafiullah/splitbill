package com.msafiullah.splitbill.viewcontrollers.item;

import android.content.Context;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.listeners.OnDecimalFieldItemContentChanged;

public class BillTotalItem extends AbstractDecimalFieldWithControlsItem {

	private static final int ITEM_LAYOUT = R.layout.bill_total_item;

	public BillTotalItem(Context context,
			OnDecimalFieldItemContentChanged listener) {

		super(context, ITEM_LAYOUT, listener);
	}

}
