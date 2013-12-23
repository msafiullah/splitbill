package com.msafiullah.splitbill.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.controllers.TaxOptionCtrl;

public class TaxOptionDialog extends Dialog {

	private static final int DIALOG_LAYOUT = R.layout.tax_option_dialog;

	private TaxOptionCtrl _taxOptionCtrl;

	private RadioGroup _optionsRadioGroup;

	public TaxOptionDialog(Context context) {
		super(context);

		_taxOptionCtrl = new TaxOptionCtrl(context);

		setContentView(DIALOG_LAYOUT);

		setTitle("Tax Option");

		_optionsRadioGroup = (RadioGroup) findViewById(R.id.rgTaxOption);

		loadTaxOption();

		setButtons();

		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
	}

	private void setButtons() {
		Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveTaxOption();
				Toast.makeText(getContext(), "Tax option saved",
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

	private void saveTaxOption() {
		boolean isServiceChargeExcludedInTax = false;
		if (_optionsRadioGroup.getCheckedRadioButtonId() == R.id.rbExcludeServiceCharge) {
			isServiceChargeExcludedInTax = true;
		}

		_taxOptionCtrl
				.saveIsServiceChargeExcludedInTax(isServiceChargeExcludedInTax);
	}

	private void loadTaxOption() {

		if (_taxOptionCtrl.isServiceChargeExcludedInTax()) {
			_optionsRadioGroup.check(R.id.rbExcludeServiceCharge);
		} else {
			_optionsRadioGroup.check(R.id.rbIncludeServiceCharge);
		}
	}

}
