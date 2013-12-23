package com.msafiullah.splitbill.dataaccess;

import java.util.ArrayList;
import java.util.Scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.msafiullah.splitbill.entity.ChargeEntity;

public class PreferenceAdapter {

	private static final String MY_PREFS = "SplitBillPref";
	private static final int MY_PREFS_MODE = 0;

	private SharedPreferences _sharedPerference;

	// private static final String tag = "PreferenceAdapter";

	public PreferenceAdapter(Context context) {
		_sharedPerference = context.getApplicationContext()
				.getSharedPreferences(MY_PREFS, MY_PREFS_MODE);
	}

	public void saveIsServiceChargeExcludedInTax(
			boolean isServiceChargeExcludedInTax) {
		Editor editor = _sharedPerference.edit();
		editor.putBoolean("isServiceChargeExcludedInTax",
				isServiceChargeExcludedInTax);
		editor.commit();
	}

	public boolean retrieveIsServiceChargeExcludedInTax() {
		return _sharedPerference.getBoolean("isServiceChargeExcludedInTax",
				false);
	}

	public void saveFavNames(ArrayList<String> nameList) {
		Editor editor = _sharedPerference.edit();

		// construct names separated by comma
		String nameListStr = "";
		for (String eachName : nameList) {
			nameListStr += eachName + ",";
		}

		editor.putString("NameList", nameListStr);

		editor.commit();
	}

	public ArrayList<String> retrieveFavNames() {
		ArrayList<String> nameList = new ArrayList<String>();

		String nameListStr = _sharedPerference.getString("NameList", "");
		Scanner sc = new Scanner(nameListStr);
		sc.useDelimiter(",");
		while (sc.hasNext()) {
			nameList.add(sc.next());
		}

		return nameList;
	}

	public void saveBillTotal(float billTotal) {
		Editor editor = _sharedPerference.edit();

		editor.putFloat("BillTotal", billTotal);

		editor.commit();
	}

	public void saveGrandTotal(float grandTotal) {
		Editor editor = _sharedPerference.edit();

		editor.putFloat("GrandTotal", grandTotal);

		editor.commit();
	}

	public void saveBillCharges(ChargeEntity discount,
			ChargeEntity serviceCharge, ChargeEntity gst) {
		Editor editor = _sharedPerference.edit();

		editor.putFloat("DiscountAmount", discount.getValue());
		editor.putInt("DiscountType", discount.getType());

		editor.putFloat("ServiceAmount", serviceCharge.getValue());
		editor.putInt("ServiceType", serviceCharge.getType());

		editor.putFloat("TaxAmount", gst.getValue());
		editor.putInt("TaxType", gst.getType());

		editor.commit();
	}

	public void saveNumPersonAndAmountEach(int numPerson, float amtEach) {
		Editor editor = _sharedPerference.edit();

		editor.putInt("NumPerson", numPerson);
		editor.putFloat("AmtEach", amtEach);

		editor.commit();
	}

	public void resetTempBillData() {
		Editor editor = _sharedPerference.edit();
		// remove total
		editor.remove("BillTotal");
		editor.remove("GrandTotal");
		// remove bill charges
		editor.remove("DiscountAmount");
		editor.remove("DiscountType");
		editor.remove("ServiceAmount");
		editor.remove("ServiceType");
		editor.remove("TaxAmount");
		editor.remove("TaxType");
		// remove simple split
		editor.remove("NumPerson");
		editor.remove("AmtEach");
		editor.commit();
	}

	public float retrieveBillTotal() {
		return _sharedPerference.getFloat("BillTotal", 0.0f);
	}

	public float retrieveGrandTotal() {
		return _sharedPerference.getFloat("GrandTotal", 0.0f);
	}

	public ChargeEntity retrieveDiscountChargeEntity() {
		return new ChargeEntity(_sharedPerference.getFloat("DiscountAmount",
				0.0f), _sharedPerference.getInt("DiscountType",
				ChargeEntity.CHARGE_TYPE_PERCENT));
	}

	public ChargeEntity retrieveServiceChargeEntity() {
		return new ChargeEntity(_sharedPerference.getFloat("ServiceAmount",
				0.0f), _sharedPerference.getInt("ServiceType",
				ChargeEntity.CHARGE_TYPE_PERCENT));
	}

	public ChargeEntity retrieveGstChargeEntity() {
		return new ChargeEntity(_sharedPerference.getFloat("TaxAmount", 0.0f),
				_sharedPerference.getInt("TaxType",
						ChargeEntity.CHARGE_TYPE_PERCENT));
	}

	public int retrieveNumPerson() {
		return _sharedPerference.getInt("NumPerson", 0);
	}

	public float retrieveAmountEach() {
		return _sharedPerference.getFloat("AmtEach", 0.0f);
	}

}
