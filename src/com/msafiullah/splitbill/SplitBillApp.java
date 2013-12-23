package com.msafiullah.splitbill;

import android.app.Application;

public class SplitBillApp extends Application {
	public static String DollarSign;

	@Override
	public void onCreate() {
		super.onCreate();
		// If we have the dollar symbols for different countries,
		// we can check the country of the device here,
		// and initialize the respective dollar sign.
		// Currently only the $ symbol exists in the String.xml resource file.
		DollarSign = getResources().getText(R.string.symbol_dollar).toString();
	}

}
