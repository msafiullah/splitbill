package com.msafiullah.splitbill.viewcontrollers.item;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.entity.ChargeEntity;
import com.msafiullah.splitbill.listeners.OnChargeItemContentChangedListener;
import com.msafiullah.splitbill.util.FloatUtil;

public class ChargeItem extends AbstractDecimalFieldWithControlsItem {

	private static final int ITEM_LAYOUT = R.layout.charge_item;

	private ChargeEntity _chargeEntity;

	private TextView _titleTextView;
	private Button _chargeTypeDollarBtn;
	private Button _chargeTypePercentBtn;
	private TextView _altChargeTextView;

	public ChargeItem(Context context, String title,
			OnChargeItemContentChangedListener contentChangedListener) {
		super(context, ITEM_LAYOUT, contentChangedListener);

		_chargeEntity = new ChargeEntity();

		initChildViewsAndSetListeners();

		_titleTextView.setText(title);

		updateAltChargeValue(0);
	}

	public void setCharge(float value, int type) {
		setAmount(value);
		setChargeType(type);
	}

	public ChargeEntity getChargeEntity() {
		return _chargeEntity;
	}

	@Override
	public void setAmount(float newAmt) {
		// store new value first because it's value is depended on by the
		// listener that is invoked by super.setAmount(...)
		_chargeEntity.setValue(newAmt);
		super.setAmount(newAmt);
	}

	public void updateAltChargeValue(float total) {
		final float altValue = _chargeEntity.computeAltValueForTotal(total);
		_altChargeTextView.setText(formatAltChargeValue(altValue));
	}

	private String formatAltChargeValue(float altValue) {
		// append DOLLAR or PERCENT symbol and set alt charge text
		if (_chargeEntity.getType() == ChargeEntity.CHARGE_TYPE_PERCENT) {
			return "$" + FloatUtil.roundUpToMoneyStr(altValue);
		} else {
			return FloatUtil.roundUpToMoneyStr(altValue) + "%";
		}
	}

	/**
	 * Sets charge type.
	 * <p>
	 * <b>SIDE EFFECTS:</b>
	 * <p>
	 * Highlight charge type button corresponding to the given type.
	 * </p>
	 * <p>
	 * Triggers on item type changed if the new type is different from old type.
	 * </p>
	 * </p>
	 * 
	 * @param type
	 *            the charge type PERCENT or DOLLAR
	 */
	private void setChargeType(int type) {

		highlightButtonForChargeType(type);

		if (_chargeEntity.getType() != type) {
			_chargeEntity.setType(type);
			if (getContentChangedListener() != null) {
				OnChargeItemContentChangedListener itemContentChangedListener = (OnChargeItemContentChangedListener) getContentChangedListener();
				itemContentChangedListener.onChargeTypeChanged(this);
			}
		}
	}

	private void highlightButtonForChargeType(int type) {
		switch (type) {
		case ChargeEntity.CHARGE_TYPE_DOLLAR:
			_chargeTypeDollarBtn
					.setBackgroundResource(R.drawable.btn_toggle_right_selected);
			_chargeTypePercentBtn
					.setBackgroundResource(R.drawable.btn_toggle_left_normal);
			break;
		case ChargeEntity.CHARGE_TYPE_PERCENT:
			_chargeTypePercentBtn
					.setBackgroundResource(R.drawable.btn_toggle_left_selected);
			_chargeTypeDollarBtn
					.setBackgroundResource(R.drawable.btn_toggle_right_normal);
			break;
		}
	}

	private void initChildViewsAndSetListeners() {
		View view = getView();

		_titleTextView = (TextView) view.findViewById(R.id.tvTitle);
		_chargeTypeDollarBtn = (Button) view.findViewById(R.id.btnDollar);
		_chargeTypePercentBtn = (Button) view.findViewById(R.id.btnPercent);
		_altChargeTextView = (TextView) view.findViewById(R.id.tvAltCharge);

		_chargeTypePercentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearThenRequestFocusOnDecimalFieldEditText();
				setChargeType(ChargeEntity.CHARGE_TYPE_PERCENT);
			}
		});
		_chargeTypeDollarBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearThenRequestFocusOnDecimalFieldEditText();
				setChargeType(ChargeEntity.CHARGE_TYPE_DOLLAR);
			}
		});

	}

}
