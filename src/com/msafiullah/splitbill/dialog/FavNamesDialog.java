package com.msafiullah.splitbill.dialog;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.controllers.FavNamesCtrl;

public class FavNamesDialog extends Dialog {

	private static final int DIALOG_LAYOUT = R.layout.name_list_dialog;

	private FavNamesCtrl _favNamesCtrl;

	private EditText _favNamesEditText;

	public FavNamesDialog(Context context) {
		super(context);

		_favNamesCtrl = new FavNamesCtrl(context);

		setContentView(DIALOG_LAYOUT);

		setTitle("Favourite Names");

		_favNamesEditText = (EditText) findViewById(R.id.etNameList);

		loadFavNames();

		setButtons();

		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
	}

	private void setButtons() {
		Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveFavNames();
				Toast.makeText(getContext(), "Names are saved",
						Toast.LENGTH_SHORT).show();
				dismiss();
			}
		});

		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void saveFavNames() {
		_favNamesCtrl.saveNames(_favNamesEditText.getText().toString());
	}

	private void loadFavNames() {
		ArrayList<String> favNames = _favNamesCtrl.getNames();
		String nameListStr = "";
		for (String eachName : favNames) {
			nameListStr += eachName + "\n";
		}
		_favNamesEditText.setText(nameListStr);
	}

}
