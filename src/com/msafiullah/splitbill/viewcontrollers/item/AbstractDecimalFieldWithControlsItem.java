package com.msafiullah.splitbill.viewcontrollers.item;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.listeners.OnDecimalFieldItemContentChanged;
import com.msafiullah.splitbill.util.FloatUtil;

public abstract class AbstractDecimalFieldWithControlsItem extends
		AbstractDecimalFieldItem {

	private Button _increaseBtn;
	private Button _decreaseBtn;

	public AbstractDecimalFieldWithControlsItem(Context context, int itemLayout,
			OnDecimalFieldItemContentChanged contentChangedListener) {
		
		super(context, itemLayout, contentChangedListener);
		
		initAndChildViewsAndSetListeners();
	}

	final private void initAndChildViewsAndSetListeners() {
		View view = getView();
		
		_increaseBtn = (Button) view.findViewById(R.id.btnUp);
		_decreaseBtn = (Button) view.findViewById(R.id.btnDown);

		_increaseBtn.setText(Html.fromHtml(view.getContext().getResources()
				.getString(R.string.symbol_up)));
		_decreaseBtn.setText(Html.fromHtml(view.getContext().getResources()
				.getString(R.string.symbol_down)));

		_increaseBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearThenRequestFocusOnDecimalFieldEditText();

				final float newAmt = FloatUtil
						.roundFloatToNearestTenCentOrOneDollarBy(getAmount(), 1);
				
				setAmount(newAmt);
			}
		});
		_decreaseBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearThenRequestFocusOnDecimalFieldEditText();

				final float newAmt = FloatUtil
						.roundFloatToNearestTenCentOrOneDollarBy(getAmount(),
								-1);
				
				if (newAmt > 0) {
					setAmount(newAmt);
				} else {
					setAmount(0);
				}
			}
		});

	}

}
