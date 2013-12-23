package com.msafiullah.splitbill.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.msafiullah.splitbill.R;

public class AboutDialog extends Dialog {

	private static final int DIALOG_LAYOUT = R.layout.about_dialog;

	public AboutDialog(Context context) {
		super(context);

		setContentView(DIALOG_LAYOUT);

		setVersionName();

		setDescription();

		setButton();

		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);

	}

	private void setButton() {
		Button dialogButton = (Button) findViewById(R.id.btnOk);

		dialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void setVersionName() {
		TextView tvVersion = (TextView) findViewById(R.id.tvVersion);

		try {
			Context context = this.getContext();
			final String versionName = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0).versionName;
			tvVersion.setText(versionName);
		} catch (NameNotFoundException e) {
			tvVersion.setText("Undertermined");
		}

	}

	private void setDescription() {
		TextView tvDesc = (TextView) findViewById(R.id.tvDesc);

		tvDesc.setText(Html.fromHtml(this.getContext().getResources()
				.getString(R.string.app_desc)));
	}

}
