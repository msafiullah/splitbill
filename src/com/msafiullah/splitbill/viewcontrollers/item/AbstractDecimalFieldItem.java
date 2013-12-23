package com.msafiullah.splitbill.viewcontrollers.item;

import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.listeners.OnDecimalFieldItemContentChanged;
import com.msafiullah.splitbill.util.FloatUtil;

public abstract class AbstractDecimalFieldItem {

	private float _decimalAmount;

	private View _view;
	private OnDecimalFieldItemContentChanged _contentChangedListener;
	private EditText _decimalFieldEditText;

	public AbstractDecimalFieldItem(Context context, int itemLayout,
			OnDecimalFieldItemContentChanged contentChangedListener) {

		_view = View.inflate(context, itemLayout, null);

		initChildViewsAndSetListeners();

		_contentChangedListener = contentChangedListener;
	}

	final public View getView() {
		return _view;
	}

	final public float getAmount() {
		return _decimalAmount;
	}

	/**
	 * Sets the new amount.
	 * <p>
	 * <b>SIDE EFFECT:</b> Triggers the on decimal amount changed listener if
	 * the new amount is different from the old amount.
	 * </p>
	 * 
	 * @param newAmt
	 */
	public void setAmount(float newAmt) {
		// store the new amount in the instance variable
		// check if new amount is different from old amount
		// -> setText(new amount) on the decimal field edit text
		// -> if new amount is zero, setText("")
		// -> fire on decimal item content changed listener

		final float oldAmt = _decimalAmount;

		_decimalAmount = newAmt;

		if (oldAmt != newAmt) {

			_decimalFieldEditText.setText(FloatUtil.formatFloatForDecimalFieldEditText(newAmt));

			if (_contentChangedListener != null) {
				_contentChangedListener.onDecimalAmountChanged(this);
			}
		}

	}

	final protected OnDecimalFieldItemContentChanged getContentChangedListener() {
		return _contentChangedListener;
	}

	final protected void clearThenRequestFocusOnDecimalFieldEditText() {
		if (_decimalFieldEditText.hasFocus()) {
			_decimalFieldEditText.clearFocus();
		}
		_decimalFieldEditText.requestFocus();
	}

	final private void setAmountFromEditTextInput(EditText editText) {
		// validate my input
		// if my input is valid
		// -> check if new amount is different from old amount
		// --> set my new amount
		// if my input is invalid
		// -> revert my input back to the old amount
		// if input is blank or zero, setText("")

		final float oldAmt = _decimalAmount;
		final String inputText = editText.getText().toString().trim();
		final String newAmtStr = (inputText.length() == 0) ? "0.0" : inputText;

		try {
			final float newAmt = FloatUtil.toFloat(newAmtStr);

			if (oldAmt != newAmt) {
				setAmount(newAmt);
			}
		} catch (NumberFormatException nfe) {

			Toast.makeText(_decimalFieldEditText.getContext(),
					newAmtStr + " is invalid", Toast.LENGTH_SHORT).show();

			editText.setText(String.valueOf(oldAmt));
		}

		if (newAmtStr.length() == 0 || _decimalAmount == 0) {
			editText.setText("");
		}
	}

	final private void initChildViewsAndSetListeners() {

		_decimalFieldEditText = (EditText) _view.findViewById(R.id.etCharge);

		_decimalFieldEditText
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus == false) {
							// User has defocused from the edit text
							setAmountFromEditTextInput((EditText) v);
						}
					}
				});
	}

}
