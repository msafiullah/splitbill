package com.msafiullah.splitbill.arrayadapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.msafiullah.splitbill.entity.BuyerItemEntity;
import com.msafiullah.splitbill.viewcontrollers.item.BuyerItem;

public class BuyerItemArrayAdapter extends ArrayAdapter<BuyerItemEntity> {
	
	private List<BuyerItemEntity> _buyerItemEntities;

	public BuyerItemArrayAdapter(Context context, int textViewResourceId,
			List<BuyerItemEntity> itemEntities) {
		super(context, textViewResourceId, itemEntities);
		_buyerItemEntities = itemEntities;
	}

	public int getCount() {
		return _buyerItemEntities.size();
	}

	public BuyerItemEntity getItem(int index) {
		return _buyerItemEntities.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		BuyerItem buyerItem = null;

		if (convertView == null) {
			buyerItem = new BuyerItem(parent);
			convertView = buyerItem.getView();
			convertView.setTag(buyerItem);
		} else {
			buyerItem = (BuyerItem) convertView.getTag();
		}

		BuyerItemEntity buyerItemEntity = getItem(position);
		buyerItem.setBuyer(buyerItemEntity.getBuyer());
		buyerItem.setAmountPayable(buyerItemEntity.getAmountPayable());

		return convertView;

	}
}
