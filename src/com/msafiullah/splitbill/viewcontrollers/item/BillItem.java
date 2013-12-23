package com.msafiullah.splitbill.viewcontrollers.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.entity.BillItemEntity;
import com.msafiullah.splitbill.listeners.BillItemEventListener;
import com.msafiullah.splitbill.util.FloatUtil;

public class BillItem implements BillItemEventListener {

	/**
	 * ID for the item is auto incremented.
	 * 
	 * <p>
	 * ID starts from 0 and increments to 1, 2, 3, ..., etc. If items are
	 * retrieved from database, the ID of the last item retrieved from database
	 * will be used to determine the next sequence of the ID.
	 * </p>
	 */
	private static int sNextItemID = 0;

	private final BillItem SELF = this;

	private final int _itemID;

	private final BillItemEntity _billItemEntity;

	private final View _view;

	private EditText _priceEditText;

	private LinearLayout _buyersLayout;
	private EditText _buyerEditText;
	private Button _addBuyerBtn;

	private TextView _serialNoTextView;
	private Button _deleteBtn;
	private Button _duplicateBtn;

	private BillItemEventListener _billItemEventListener;

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	public BillItem(Context context, int serialNo) {
		this(context, sNextItemID, serialNo);
	}

	public BillItem(Context context, int itemID, int serialNo, float price,
			ArrayList<String> buyers) {

		this(context, itemID, serialNo);

		setPrice(price); // Does not trigger the price changed listener
		addBuyers(buyers); // Does not trigger the buyers added listener
		
		enableDelete(true);
		enableDuplicate(true);
	}

	private BillItem(Context context, int itemID, int serialNo) {

		_itemID = itemID;
		sNextItemID = itemID + 1;

		_billItemEntity = new BillItemEntity(serialNo);

		_view = View.inflate(context, R.layout.bill_item, null);

		initItemChildViewsAndAddListeners();
		initPriceWidgetChildViewsAndAddListeners();
		initBuyerWidgetChildViewsAndAddListeners();

		setSerialNo(serialNo);

		enableDelete(false);
		enableDuplicate(false);
		
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	public float getPrice() {
		return _billItemEntity.getPrice();
	}

	/**
	 * Sets the price for this bill item.
	 * <p>
	 * <b>Triggers:</b>
	 * <ol>
	 * <li>{@link #triggerOnPriceChanged(float, float)} if the new price differs
	 * from old price.</li>
	 * </ol>
	 * </p>
	 * 
	 * @param newAmt
	 */
	public void setPrice(float newAmt) {
		// store the new amount in the instance variable
		// check if new amount is different from old amount
		// -> setText(new amount) on the decimal field edit text
		// -> if new amount is zero, setText("")
		// -> fire on decimal item content changed listener

		final float oldAmt = getPrice();

		_billItemEntity.setPrice(newAmt);

		if (oldAmt != newAmt) {

			_priceEditText.setText(FloatUtil
					.formatFloatForDecimalFieldEditText(newAmt));

			onPriceChanged(SELF, newAmt - oldAmt);
		}

	}

	/**
	 * 
	 * ONLY CALLED when this item's price edit text is defocused.
	 * <p>
	 * <b>Triggers:</b>
	 * <ol>
	 * <li>Indirectly {@link #triggerOnPriceChanged(float, float)} if
	 * {@link #setPrice(float)} is invoked.</li>
	 * </ol>
	 * <p>
	 * 
	 * @param editText
	 */
	private void setPriceFromEditTextInput(EditText editText) {
		// validate my input
		// if my input is valid
		// -> check if new amount is different from old amount
		// --> set my new amount
		// if my input is invalid
		// -> revert my input back to the old amount
		// if input is blank or zero, setText("")

		final float oldAmt = getPrice();
		final String inputText = editText.getText().toString().trim();
		final String newAmtStr = (inputText.length() == 0) ? "0.0" : inputText;

		try {
			final float newAmt = FloatUtil.toFloat(newAmtStr);

			if (oldAmt != newAmt) {
				setPrice(newAmt);
			}
		} catch (NumberFormatException nfe) {

			Toast.makeText(_priceEditText.getContext(),
					newAmtStr + " is invalid", Toast.LENGTH_SHORT).show();

			editText.setText(String.valueOf(oldAmt));
		}

		if (newAmtStr.length() == 0 || getPrice() == 0) {
			editText.setText("");
		}
	}

	private void initPriceWidgetChildViewsAndAddListeners() {

		_priceEditText = (EditText) _view.findViewById(R.id.etItemPrice);

		// Set On Focus Change listener for price edit text
		_priceEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus == false) {
					// User has defocused from the edit text
					setPriceFromEditTextInput((EditText) v);
				}
			}
		});
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	public List<String> getBuyers() {
		return _billItemEntity.getBuyers();
	}

	/**
	 * CALLED
	 * <p>
	 * 1. When user clicks the add buyer button
	 * </p>
	 * <p>
	 * 2. When user has inputed some text and then defocuses from the buyer edit
	 * text.
	 * </p>
	 * <p>
	 * <b>SIDE EFFECT:<b> Might call the addBuyers(buyers) method which will
	 * trigger the buyers added listener.
	 * 
	 * @param editText
	 */
	private void addBuyerFromBuyerEditText(EditText editText) {
		final String inputText = _buyerEditText.getText().toString()
				.replaceAll("[^a-zA-Z0-9]+", "").trim()
				.toUpperCase(Locale.ENGLISH);

		if (inputText.length() != 0) {

			if (_billItemEntity.hasBuyer(inputText)) {
				Toast.makeText(editText.getContext(), inputText + " exists",
						Toast.LENGTH_SHORT).show();
			} else {
				List<String> tempBuyers = new ArrayList<String>();
				tempBuyers.add(inputText);
				addBuyers(tempBuyers);
			}

			editText.setText("");

		}

	}

	/**
	 * Add a bunch of buyers to this item.
	 * <p>
	 * <b>SIDE EFFECT:</b>Triggers the buyers added listener.
	 * </p>
	 * 
	 * @param buyers
	 */
	public void addBuyers(List<String> buyers) {
		for (String eachBuyer : buyers) {
			eachBuyer = eachBuyer.toUpperCase(Locale.ENGLISH);
			addBuyerToView(eachBuyer);
			_billItemEntity.addBuyer(eachBuyer);
		}

		onBuyersAdded(SELF, buyers);
	}

	/**
	 * ONLY CALLED whenever a buyer is added.
	 * 
	 * @param buyer
	 */
	private void addBuyerToView(final String buyer) {

		TextView buyerTextView = (TextView) View.inflate(
				_buyersLayout.getContext(), R.layout.buyer_textview, null);

		buyerTextView.setText(buyer);
		buyerTextView.setTag(buyer);

		buyerTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View buyerView) {
				removeBuyer((TextView) buyerView);
			}
		});

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(11, 0, 11, 11);
		buyerTextView.setLayoutParams(params);

		_buyersLayout.addView(buyerTextView);
	}

	/**
	 * ONLY CALLED when user deletes a buyer by clicking on the buyer's name to
	 * delete him.
	 * <p>
	 * <b>SIDE EFFECT:</b>Triggers the buyer deleted listener.
	 * </p>
	 * 
	 * @param buyerTextView
	 */
	private void removeBuyer(TextView buyerTextView) {

		_buyersLayout.removeView(buyerTextView);

		final String buyer = buyerTextView.getText().toString();

		_billItemEntity.removeBuyer(buyer);

		onBuyerDeleted(SELF, buyer);
	}

	public void removeBuyers(List<String> buyersToRemove){
		for(String eachBuyerToRemove : buyersToRemove){
			TextView buyerTextView = (TextView)_buyersLayout.findViewWithTag(eachBuyerToRemove);
			removeBuyer(buyerTextView);
		}
	}
	
	/**
	 * ONLY CALLED when the focus of buyer edit text changes.
	 * 
	 * @param isBuyerEditTextFocused
	 */
	private void updateAddBuyerButtonBackground(boolean isBuyerEditTextFocused) {
		if (isBuyerEditTextFocused) {
			_addBuyerBtn
					.setBackgroundResource(R.drawable.btn_toggle_right_selected);
		} else {
			_addBuyerBtn
					.setBackgroundResource(R.drawable.btn_toggle_right_normal);
		}
	}

	private void initBuyerWidgetChildViewsAndAddListeners() {
		_buyersLayout = (LinearLayout) _view
				.findViewById(R.id.layoutBuyerNames);

		_buyerEditText = (EditText) _view.findViewById(R.id.etBuyer);
		_buyerEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				updateAddBuyerButtonBackground(hasFocus);

				if (hasFocus == false) {
					addBuyerFromBuyerEditText(((EditText) view));
				}
			}
		});

		_addBuyerBtn = ((Button) _view.findViewById(R.id.btnAddBuyer));
		_addBuyerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!_buyerEditText.hasFocus()) {
					_buyerEditText.requestFocus();
				}
				if (_buyerEditText.getText().toString().trim().length() != 0) {
					addBuyerFromBuyerEditText(_buyerEditText);
				} else {
					onShowSelectBuyersDialogEvent(SELF);
				}
			}
		});
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	private void clearThenRequestFocusOnCurrentEditTextWithFocus() {
		if (_priceEditText.hasFocus()) {
			_priceEditText.clearFocus();
			_priceEditText.requestFocus();
		} else if (_buyerEditText.hasFocus()) {
			_buyerEditText.clearFocus();
			_buyerEditText.requestFocus();
		}
	}

	public int getSerialNo() {
		return _billItemEntity.getSerialNo();
	}

	public void setSerialNo(int serialNo) {
		_billItemEntity.setSerialNo(serialNo);
		_serialNoTextView.setText("#" + serialNo);
	}

	private void initItemChildViewsAndAddListeners() {

		_serialNoTextView = (TextView) _view.findViewById(R.id.tvItemNum);
		_deleteBtn = (Button) _view.findViewById(R.id.btnDelete);
		_duplicateBtn = (Button) _view.findViewById(R.id.btnDuplicate);

		_deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onDelete(SELF);
			}
		});
		_duplicateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onDuplicate(SELF);
			}
		});

		enableDelete(false);
		enableDuplicate(false);
	}

	private void enableDelete(boolean enable) {
		if (enable == false) {
			_deleteBtn.setEnabled(false);
			_deleteBtn.setVisibility(View.INVISIBLE);
		} else {
			_deleteBtn.setEnabled(true);
			_deleteBtn.setVisibility(View.VISIBLE);
		}

	}

	private void enableDuplicate(boolean enable) {
		if (enable == false) {
			_duplicateBtn.setEnabled(false);
			_duplicateBtn.setVisibility(View.INVISIBLE);
		} else {
			_duplicateBtn.setEnabled(true);
			_duplicateBtn.setVisibility(View.VISIBLE);
		}
	}

	public int getItemID() {
		return _itemID;
	}

	public View getView() {
		return _view;
	}

	public boolean isEmpty() {
		return _billItemEntity.isEmpty();
	}

	public void setBillItemEventListener(
			BillItemEventListener billItemEventListener) {
		_billItemEventListener = billItemEventListener;
	}

	public static void reset() {
		sNextItemID = 0;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	@Override
	public void onPriceChanged(BillItem item, float priceChange) {
		if(_billItemEventListener!=null){
			_billItemEventListener.onPriceChanged(item, priceChange);
		}

		onContentChanged(item);
		
		if(isEmpty()){
			onBecomesEmpty(item);
		}
	}

	@Override
	public void onBuyerDeleted(BillItem item, String deletedBuyer) {
		if(_billItemEventListener!=null){
			_billItemEventListener.onBuyerDeleted(item, deletedBuyer);
		}
		
		onContentChanged(item);
		
		if(isEmpty()){
			onBecomesEmpty(item);
		}
	}

	@Override
	public void onBuyersAdded(BillItem item, List<String> addedBuyers) {
		if(_billItemEventListener!=null){
			_billItemEventListener.onBuyersAdded(item, addedBuyers);
		}
		
		onContentChanged(item);
	}

	@Override
	public void onBecomesEmpty(BillItem item) {
		
		enableDelete(false);
		enableDuplicate(false);
		
		if(_billItemEventListener!=null){
			_billItemEventListener.onBecomesEmpty(item);
		}
	}

	@Override
	public void onContentChanged(BillItem item) {
		
		if(!isEmpty()){
			enableDelete(true);
			enableDuplicate(true);
		}
		
		if(_billItemEventListener!=null){
			_billItemEventListener.onContentChanged(item);
		}
	}

	@Override
	public void onDelete(BillItem item) {
		clearThenRequestFocusOnCurrentEditTextWithFocus();
		if(_billItemEventListener!=null){
			_billItemEventListener.onDelete(item);
		}
	}

	@Override
	public void onDuplicate(BillItem item) {
		clearThenRequestFocusOnCurrentEditTextWithFocus();
		
		if(_billItemEventListener!=null){
			_billItemEventListener.onDuplicate(item);
		}
	}

	@Override
	public void onShowSelectBuyersDialogEvent(BillItem item) {
		if(_billItemEventListener!=null){
			_billItemEventListener.onShowSelectBuyersDialogEvent(item);
		}
	}

}
