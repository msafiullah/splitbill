package com.msafiullah.splitbill.viewcontrollers.item;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.listeners.OnUniformSplitItemContentChangedListener;
import com.msafiullah.splitbill.util.FloatUtil;

public class UniformSplitItem extends AbstractDecimalFieldWithControlsItem {

	private static final int ITEM_LAYOUT = R.layout.person_amount_item;

	private int _numPerson;

	private EditText _numPersonEditText;
	private Button _increaseNumPersonBtn;
	private Button _decreaseNumPersonBtn;

	public UniformSplitItem(Context context,
			OnUniformSplitItemContentChangedListener contentChangedListener) {

		super(context, ITEM_LAYOUT, contentChangedListener);

		initAndAddChildViewsAndListeners();
	}

	public int getNumPerson() {
		return _numPerson;
	}

	/**
	 * Sets the new number of person.
	 * <p>
	 * <b>SIDE EFFECT:</b> Triggers the on num person changed listener if the
	 * new number is different from the old number.
	 * </p>
	 * 
	 * @param newNum
	 */
	public void setNumPerson(int newNum) {
		final int oldNum = _numPerson;

		_numPerson = newNum;

		if (newNum > 0) {
			_numPersonEditText.setText(String.valueOf(newNum));
		} else {
			_numPerson = 0;
			_numPersonEditText.setText("");
		}

		if (oldNum != newNum) {
			if (getContentChangedListener() != null) {
				OnUniformSplitItemContentChangedListener listener = (OnUniformSplitItemContentChangedListener) getContentChangedListener();
				listener.onNumPersonChanged(this);
			}
		}
	}

	@Override
	public void setAmount(float newAmt) {
		if (newAmt > 0) {
			super.setAmount(FloatUtil.roundUpToMoney(newAmt));
		} else {
			super.setAmount(0);
		}
	}

	public void updateAmountEachPaysForTotal(float total) {
		if (_numPerson > 0) {
			setAmount(total / _numPerson);
		} else {
			setAmount(0);
		}
	}

	public float computeSumForAllPersons() {
		return _numPerson * getAmount();
	}

	private void clearThenRequestFocusOnNumPersonEditText() {
		if (_numPersonEditText.hasFocus()) {
			_numPersonEditText.clearFocus();
		}
		_numPersonEditText.requestFocus();
	}

	private void setNumPersonFromEditTextInput(EditText numPersonEditText) {
		// validate my input
		// if my input is valid
		// -> check if new amount is different from old amount
		// --> set my new value
		// if my input is invalid
		// -> revert my input back to the old amount
		// if input is blank or zero, setText("")

		final int oldAmt = _numPerson;
		final String inputText = numPersonEditText.getText().toString().trim();
		final String newAmtStr = (inputText.length() == 0) ? "0" : inputText;

		try {
			final int newAmt = Integer.parseInt(newAmtStr);

			if (oldAmt != newAmt) {
				setNumPerson(newAmt);
			}
		} catch (NumberFormatException nfe) {
			Toast.makeText(_numPersonEditText.getContext(),
					newAmtStr + " is invalid", Toast.LENGTH_SHORT).show();
			numPersonEditText.setText(String.valueOf(oldAmt));
		}

		if (newAmtStr.length() == 0 || _numPerson == 0) {
			numPersonEditText.setText("");
		}
	}

	private void initAndAddChildViewsAndListeners() {
		View view = getView();

		_numPersonEditText = (EditText) view.findViewById(R.id.etNumPerson);

		_increaseNumPersonBtn = (Button) view
				.findViewById(R.id.btnUpNumOfPeople);
		_decreaseNumPersonBtn = (Button) view
				.findViewById(R.id.btnDownNumOfPeople);

		_increaseNumPersonBtn.setText(Html.fromHtml(view.getContext()
				.getResources().getString(R.string.symbol_up)));
		_decreaseNumPersonBtn.setText(Html.fromHtml(view.getContext()
				.getResources().getString(R.string.symbol_down)));

		_increaseNumPersonBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearThenRequestFocusOnNumPersonEditText();

				setNumPerson(_numPerson + 1);
			}
		});
		_decreaseNumPersonBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearThenRequestFocusOnNumPersonEditText();
				if (_numPerson > 0) {
					setNumPerson(_numPerson - 1);
				} else {
					setNumPerson(0);
				}
			}
		});

		_numPersonEditText
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus == false) {
							// user has defocused the num person edit text
							setNumPersonFromEditTextInput((EditText) v);
						}
					}
				});
	}

}
