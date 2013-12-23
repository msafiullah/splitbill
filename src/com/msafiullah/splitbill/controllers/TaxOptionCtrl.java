package com.msafiullah.splitbill.controllers;

import android.content.Context;

import com.msafiullah.splitbill.dataaccess.PreferenceAdapter;

public class TaxOptionCtrl {

	private boolean _isServiceChargeExcludedInTax = false;
	private PreferenceAdapter _prefAdapter;

	public TaxOptionCtrl(Context context) {
		_prefAdapter = new PreferenceAdapter(context);
		_isServiceChargeExcludedInTax = _prefAdapter.retrieveIsServiceChargeExcludedInTax();
	}

	public void saveIsServiceChargeExcludedInTax(boolean isServiceChargeExcludedInTax) {
		_isServiceChargeExcludedInTax = isServiceChargeExcludedInTax;
		_prefAdapter.saveIsServiceChargeExcludedInTax(isServiceChargeExcludedInTax);
	}
	
	public boolean isServiceChargeExcludedInTax() {
		return _isServiceChargeExcludedInTax;
	}

}
