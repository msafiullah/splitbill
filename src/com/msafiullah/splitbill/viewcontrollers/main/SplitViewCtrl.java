package com.msafiullah.splitbill.viewcontrollers.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.arrayadapters.BuyerItemArrayAdapter;
import com.msafiullah.splitbill.dataaccess.TempDbAdapter;
import com.msafiullah.splitbill.entity.BuyerItemEntity;
import com.msafiullah.splitbill.viewcontrollers.item.TotalDisplayItem;

public class SplitViewCtrl {

	private final String tag = "SplitViewCtrl";

	private View _view;
	private TotalDisplayItem _grandTotal;
	private ListView _listView;
	private List<BuyerItemEntity> _buyersList = new ArrayList<BuyerItemEntity>();
	private BuyerItemArrayAdapter _adapter;
	private View _progressView;

	public SplitViewCtrl(View view) {
		_view = view;

		_progressView = view.findViewById(R.id.circularProgressLayout);

		_grandTotal = new TotalDisplayItem(
				(TextView) view.findViewById(R.id.tvGrandTotal));

		// set up list view
		_buyersList.add(new BuyerItemEntity("ITEM #1", 0));
		_adapter = new BuyerItemArrayAdapter(view.getContext(),
				R.layout.buyer_item, _buyersList);
		_listView = (ListView) view.findViewById(R.id.lvBuyerList);
		_listView.setAdapter(_adapter);
	}

	public void refreshListAndUpdateGrandTotal() {
		Log.d(tag, "refreshListAndUpdateGrandTotal()");

		new RefreshListAndUpdateGrandTotal().execute();

	}

	private void showProgressAndHideList() {
		_listView.setVisibility(View.INVISIBLE);
		_progressView.setVisibility(View.VISIBLE);
	}

	private void hideProgressAndShowList() {
		_listView.setVisibility(View.VISIBLE);
		_progressView.setVisibility(View.GONE);
	}

	private float computeGrandTotal(final List<BuyerItemEntity> buyersList) {
		float total = 0;
		for (BuyerItemEntity eachBuyer : buyersList) {
			total += eachBuyer.getAmountPayable();
		}
		return total;
	}

	private class RefreshListAndUpdateGrandTotal extends
			AsyncTask<Void, Void, List<BuyerItemEntity>> {

		protected void onPreExecute() {
			// Show progress
			showProgressAndHideList();
		}

		@Override
		protected List<BuyerItemEntity> doInBackground(Void... params) {
			Context context = _view.getContext().getApplicationContext();
			TempDbAdapter dbAdapter = new TempDbAdapter(context);

			List<BuyerItemEntity> list = dbAdapter.retrieveBuyersAndAmounts();

			return list;
		}

		protected void onPostExecute(List<BuyerItemEntity> buyersList) {
			if (!buyersList.isEmpty()) {
				_buyersList.clear();
				_buyersList.addAll(buyersList);
				_adapter.notifyDataSetChanged();
			}

			// update grand total
			_grandTotal.setTotal(computeGrandTotal(_buyersList));

			// Hide progress
			hideProgressAndShowList();
		}

	}

}
