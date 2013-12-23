package com.msafiullah.splitbill.controllers;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import android.content.Context;

import com.msafiullah.splitbill.dataaccess.PreferenceAdapter;

public class FavNamesCtrl {

	private ArrayList<String> _names = new ArrayList<String>();
	private PreferenceAdapter _prefAdapter;

	public FavNamesCtrl(Context context) {
		_prefAdapter = new PreferenceAdapter(context);
		_names = _prefAdapter.retrieveFavNames();
	}

	public void saveNames(String namePerLineStr) {
		_names.clear();
		Scanner sc = new Scanner(namePerLineStr);
		sc.useDelimiter("\r\n|\n");
		while (sc.hasNext()) {
			String eachName = sc.next();
			if (eachName != null && eachName.length() != 0) {
				eachName = eachName.replaceAll("[^a-zA-Z0-9]+", "").trim()
						.toUpperCase(Locale.ENGLISH);
				if (eachName.length() != 0 && !_names.contains(eachName)) {
					_names.add(eachName);
				}
			}
		}
		
		_prefAdapter.saveFavNames(_names);
	}

	public ArrayList<String> getNames() {
		return _names;
	}

}
