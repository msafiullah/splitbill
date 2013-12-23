package com.msafiullah.splitbill.util;

import java.text.DecimalFormat;

public class FloatUtil {
	
	public static String roundUpToMoneyStr(float amt) {
		DecimalFormat decimalFormatter = new DecimalFormat("0.00");
		return decimalFormatter.format(amt);
	}

	public static float roundUpToMoney(float amt) {
		return Float.parseFloat(roundUpToMoneyStr(amt));
	}

	public static float toFloat(String string) throws NumberFormatException {
		float val = 0;
		boolean keepTrying = true;

		for (int i = 0; keepTrying; i++) {
			try {
				string = string.trim();
				val = Float.parseFloat(string);
				keepTrying = false;
			} catch (NumberFormatException nfe) {
				if (i < 1) { // trying for the 1st time
					// replace all non numeric characters with a decimal point &
					// try again
					string = string.replaceAll("[^\\d]", ".");
				} else if (i < 2) { // trying for the 2nd time
					// remove any trailing decimal points & try again
					int indexOfDecimalPoint = string.indexOf('.');
					String n = string.substring(0, indexOfDecimalPoint + 1);
					String d = string.substring(indexOfDecimalPoint + 1);
					d = d.replace(".", "");
					string = n + d;
				} else if (i < 3) { // trying for the 3rd time
					if (string.equals(".") || string.length() == 0) {
						string = "0";
					}
				} else { // if error persists the 3rd time, just give up
					// give up trying & throw exception
					throw new NumberFormatException(string + " is invalid");
				}
			}
		}

		return val;
	}

	public static float roundFloatToNearestTenCentOrOneDollarBy(float value,
			int roundBy) {
		// check if charge value has decimal portion (e.g. 1.1, NOT 1.0)
		if (value > (int) value) {
			// round to nearest ten cents and increment by 1
			// e.g. 1.123 -> 1.2, 1.987 -> 2.0
			return ((int) (value * 10.0f) + roundBy) / 10.0f;
		} else {
			// increment the whole number portion
			return (int) value + roundBy;
		}
	}
	
	public static String formatFloatForDecimalFieldEditText(float amount) {
		if (amount == 0) {
			return "";
		}

		// get rid of the .0 in 1.0, and display a non decimal number e.g. 1
		if (amount == (int) amount) {
			return String.valueOf((int) amount);
		} else {
			return String.valueOf(amount);
		}
	}

}
