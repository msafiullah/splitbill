package com.msafiullah.splitbill.viewcontrollers.main;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.dataaccess.PreferenceAdapter;
import com.msafiullah.splitbill.entity.ChargeEntity;
import com.msafiullah.splitbill.listeners.OnChargeItemContentChangedListener;
import com.msafiullah.splitbill.listeners.OnDecimalFieldItemContentChanged;
import com.msafiullah.splitbill.listeners.OnUniformSplitItemContentChangedListener;
import com.msafiullah.splitbill.util.FloatUtil;
import com.msafiullah.splitbill.viewcontrollers.item.AbstractDecimalFieldItem;
import com.msafiullah.splitbill.viewcontrollers.item.BillTotalItem;
import com.msafiullah.splitbill.viewcontrollers.item.ChargeItem;
import com.msafiullah.splitbill.viewcontrollers.item.TotalAndSettlementDisplayItem;
import com.msafiullah.splitbill.viewcontrollers.item.UniformSplitItem;

public class ChargeViewCtrl {

	public final String tag = "ChargeView";

	private ChargeItem _serviceChargeItem, _gstItem, _discountItem;
	private TotalAndSettlementDisplayItem _grandTotalItem;
	private BillTotalItem _billTotalItem;
	private UniformSplitItem _uniformSplitItem;
	private PreferenceAdapter _prefAdapter;

	public ChargeViewCtrl(View view) {
		// initialize member variables
		Context context = view.getContext();

		_prefAdapter = new PreferenceAdapter(context);

		_serviceChargeItem = new ChargeItem(context, "Service / Tip",
				new OnChargeItemContentChangedListener() {

					private void onContentChanged(ChargeItem item) {
						updateGrandTotal();
						updateAmountEach();
						// get & set alt charge
						updateAltServiceCharge();
						updateAltGst();
					}

					@Override
					public void onDecimalAmountChanged(AbstractDecimalFieldItem item) {
						onContentChanged((ChargeItem) item);
					}

					@Override
					public void onChargeTypeChanged(ChargeItem item) {
						onContentChanged(item);
					}
				});
		_gstItem = new ChargeItem(context, "Taxes",
				new OnChargeItemContentChangedListener() {

					private void onContentChanged(ChargeItem item) {
						updateGrandTotal();
						updateAmountEach();
						// get & set alt charge
						updateAltGst();
					}

					@Override
					public void onDecimalAmountChanged(AbstractDecimalFieldItem item) {
						onContentChanged((ChargeItem) item);
					}

					@Override
					public void onChargeTypeChanged(ChargeItem item) {
						onContentChanged(item);
					}
				});
		_discountItem = new ChargeItem(context, "Discount",
				new OnChargeItemContentChangedListener() {
					private void onContentChanged(ChargeItem item) {
						updateGrandTotal();
						updateAmountEach();
						// get & set alt charge
						updateAltServiceCharge();
						updateAltDiscount();
						updateAltGst();
					}

					@Override
					public void onDecimalAmountChanged(AbstractDecimalFieldItem item) {
						onContentChanged((ChargeItem) item);
					}

					@Override
					public void onChargeTypeChanged(ChargeItem item) {
						onContentChanged(item);
					}
				});
		_billTotalItem = new BillTotalItem(context,
				new OnDecimalFieldItemContentChanged() {
					@Override
					public void onDecimalAmountChanged(AbstractDecimalFieldItem item) {
						// update grand total
						updateGrandTotal();
						updateAmountEach();
						// update charge items' alt charge amount
						updateAltDiscount();
						updateAltServiceCharge();
						updateAltGst();
					}
				});
		_grandTotalItem = new TotalAndSettlementDisplayItem(
				(TextView) view.findViewById(R.id.tvGrandTotal),
				(TextView) view.findViewById(R.id.tvStatus));

		_uniformSplitItem = new UniformSplitItem(context,
				new OnUniformSplitItemContentChangedListener() {
					@Override
					public void onNumPersonChanged(UniformSplitItem item) {
						final float originalGrandTotal = FloatUtil
								.roundUpToMoney(computeGrandTotal());
						// update amount each
						item.updateAmountEachPaysForTotal(originalGrandTotal);
					}

					@Override
					public void onDecimalAmountChanged(
							AbstractDecimalFieldItem decimalFieldItem) {
						UniformSplitItem item = (UniformSplitItem) decimalFieldItem;

						float settlementAmount = FloatUtil.roundUpToMoney(item
								.computeSumForAllPersons());
						if (settlementAmount > 0) {
							_grandTotalItem.updateSettlementDisplay(settlementAmount);
						} else {
							_grandTotalItem.resetSettlementDisplay();
						}
					}
				});

		// add charge items to layout
		LinearLayout myChargeLayout = (LinearLayout) view
				.findViewById(R.id.layoutCharge);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(11, 9, 11, 9);
		myChargeLayout.addView(_billTotalItem.getView(), params);
		params.setMargins(11, 0, 11, 9);
		myChargeLayout.addView(_discountItem.getView(), params);
		myChargeLayout.addView(_serviceChargeItem.getView(), params);
		myChargeLayout.addView(_gstItem.getView(), params);
		myChargeLayout.addView(_uniformSplitItem.getView(), params);
	}

	public void saveData() {
		// Save bill total & grand total
		_prefAdapter.saveBillTotal(_billTotalItem.getAmount());
		_prefAdapter.saveGrandTotal(_grandTotalItem.getTotal());

		// Save bill charges: discount, service charge & GST
		_prefAdapter.saveBillCharges(_discountItem.getChargeEntity(),
				_serviceChargeItem.getChargeEntity(), _gstItem.getChargeEntity());

		// Save num of person & amount each
		_prefAdapter.saveNumPersonAndAmountEach(_uniformSplitItem.getNumPerson(),
				_uniformSplitItem.getAmount());
	}

	public void loadData() {
		// Load bill total
		_billTotalItem.setAmount(_prefAdapter.retrieveBillTotal());

		// Load bill charges: discount, service charge & GST
		ChargeEntity charge = _prefAdapter.retrieveDiscountChargeEntity();
		_discountItem.setCharge(charge.getValue(), charge.getType());

		charge = _prefAdapter.retrieveServiceChargeEntity();
		_serviceChargeItem.setCharge(charge.getValue(), charge.getType());

		charge = _prefAdapter.retrieveGstChargeEntity();
		_gstItem.setCharge(charge.getValue(), charge.getType());

		// Load num of person & amount each
		_uniformSplitItem.setNumPerson(_prefAdapter.retrieveNumPerson());
		_uniformSplitItem.setAmount(_prefAdapter.retrieveAmountEach());

		updateGrandTotal();
	}

	/** Calculations **/

	private float computeTotalAfterDiscount() {
		final float total = _billTotalItem.getAmount();
		return (total - _discountItem.getChargeEntity()
				.computeChargeForTotal(total));
	}

	private float computeTotalAfterServiceCharge() {
		float total = computeTotalAfterDiscount();
		return (total + _serviceChargeItem.getChargeEntity().computeChargeForTotal(
				total));
	}

	private float computeGrandTotal() {
		// compute including service charge
		float total = computeTotalAfterServiceCharge();
		total += _gstItem.getChargeEntity().computeChargeForTotal(total);

		// compute tax excluding service charge
		if (_prefAdapter.retrieveIsServiceChargeExcludedInTax()) {
			total = computeTotalAfterDiscount();
			total += _serviceChargeItem.getChargeEntity().computeChargeForTotal(
					total)
					+ _gstItem.getChargeEntity().computeChargeForTotal(total);
		}
		return total;
	}

	private void updateAltDiscount() {
		_discountItem.updateAltChargeValue(_billTotalItem.getAmount());
	}

	private void updateAltServiceCharge() {
		_serviceChargeItem.updateAltChargeValue(computeTotalAfterDiscount());
	}

	private void updateAltGst() {
		// total includes service charge
		float total = computeTotalAfterServiceCharge();

		// total excludes service charge
		if (_prefAdapter.retrieveIsServiceChargeExcludedInTax()) {
			total = computeTotalAfterDiscount();
		}
		_gstItem.updateAltChargeValue(total);
	}

	private void updateGrandTotal() {
		_grandTotalItem.setTotal(computeGrandTotal());
	}

	private void updateAmountEach() {
		_uniformSplitItem.updateAmountEachPaysForTotal(_grandTotalItem.getTotal());
	}

}
