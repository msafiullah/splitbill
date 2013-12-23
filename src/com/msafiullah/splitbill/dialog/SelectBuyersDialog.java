package com.msafiullah.splitbill.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.dataaccess.PreferenceAdapter;
import com.msafiullah.splitbill.dataaccess.TempDbAdapter;
import com.msafiullah.splitbill.listeners.OnBuyersSelectedListener;

public class SelectBuyersDialog extends Dialog {

	private static final int DIALOG_LAYOUT = R.layout.buyers_dialog;

	private List<String> _names;

	private ListView _selectBuyersListView;
	private ArrayAdapter<String> _listViewAdapter;
	private CheckBox _selectAllCheckBox;
	private OnBuyersSelectedListener _buyersSelectedListener;
	private List<String> _selectedNames;

	/*
	 * 
	 * 
	 * 
	 * 
	 */

	public SelectBuyersDialog(Context context, List<String> existingBuyers,
			OnBuyersSelectedListener buyersSelectedListener) {
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(DIALOG_LAYOUT);

		_buyersSelectedListener = buyersSelectedListener;

		_names = new ArrayList<String>();
		_names.add("Loading...");
		_selectedNames = new ArrayList<String>();
		_selectedNames.addAll(existingBuyers);

		initListView();
		initSelectAllCheckBox();
		initButtons();

		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		new PopulateListViewAsyncTask().execute();

	}

	/*
	 * 
	 * 
	 * 
	 * 
	 */

	private void initListView() {

		_selectBuyersListView = (ListView) findViewById(R.id.lvBuyerList);

		_listViewAdapter = new ArrayAdapter<String>(this.getContext(),
				R.layout.simple_list_item_multiple_choice, _names);

		_selectBuyersListView.setAdapter(_listViewAdapter);

		_selectBuyersListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		_selectBuyersListView.setItemsCanFocus(false);

		_selectBuyersListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// NOTE: onItemClick is called before the view is selected
				onListViewItemClicked(_names.get(position));
			}
		});

	}

	private void initSelectAllCheckBox() {
		_selectAllCheckBox = (CheckBox) findViewById(R.id.cbSelect);
	
		_selectAllCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setSelectionForAllNames(_selectAllCheckBox.isChecked());
			}
		});
	}

	private void initButtons() {
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		Button btnDone = (Button) findViewById(R.id.btnDone);
	
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		btnDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO why am i passing null value?
				_buyersSelectedListener.onBuyerSelectionCompleted(null,
						_selectedNames);
				dismiss();
			}
		});
	}

	private void onListViewItemClicked(String name) {
		if (!_selectedNames.contains(name)) {
			addToSelectedName(name);
		} else {
			removeFromSelectedName(name);
		}
		
		updateSelectionForSelectAllCheckBox();
	}

	private void addToSelectedName(String name){
		_selectedNames.add(name);
	}
	
	private void removeFromSelectedName(String name){
		_selectedNames.remove(name);
	}
	
	private void addAllToSelectedNames(List<String> names){
		_selectedNames.clear();
		_selectedNames.addAll(names);
	}
	
	private void removeAllFromSelectedNames(){
		_selectedNames.clear();
	}

	private void setSelectionForAllNames(boolean isSelectAllCheckBoxBeingChecked) {
		int listItemsCount = _selectBuyersListView.getCount();
		for (int i = 0; i < listItemsCount; i++) {
			_selectBuyersListView.setItemChecked(i, isSelectAllCheckBoxBeingChecked);
		}
		
		if(isSelectAllCheckBoxBeingChecked){
			addAllToSelectedNames(_names);
		} else {
			removeAllFromSelectedNames();
		}
	}

	private void checkSelectedNamesOnListView() {
		ListIterator<String> namesIterator = _names.listIterator();
		for (int i = 0; namesIterator.hasNext(); i++) {
			if (_selectedNames.contains(namesIterator.next())) {
				_selectBuyersListView.setItemChecked(i, true);
			}
		}
	}

	private void updateSelectionForSelectAllCheckBox() {
		_selectAllCheckBox.setEnabled(false);
		if (_selectedNames.isEmpty() || !_selectedNames.containsAll(_names)) {
			_selectAllCheckBox.setChecked(false);
		} else {
			_selectAllCheckBox.setChecked(true);
		}
		_selectAllCheckBox.setEnabled(true);
	}

	private void populateListViewWithNames(List<String> names) {
		_names.clear();
		_names.addAll(names);
		_listViewAdapter.notifyDataSetChanged();
	}

	private List<String> combineNames(List<String> names1, List<String> names2) {
		List<String> combinedNames = new ArrayList<String>();
		combinedNames.addAll(names1);
		combinedNames.removeAll(names2);
		combinedNames.addAll(names2);

		return combinedNames;
	}

	private class PopulateListViewAsyncTask extends
			AsyncTask<Void, Void, List<String>> {

		@Override
		protected List<String> doInBackground(Void... params) {

			TempDbAdapter dbAdapter = new TempDbAdapter(getContext());
			PreferenceAdapter prefAdapter = new PreferenceAdapter(getContext());

			// get a list of distinct names
			List<String> distinctBuyerNames = dbAdapter
					.retrieveDistinctBuyers();
			List<String> favNames = prefAdapter.retrieveFavNames();
			List<String> combinedNames = combineNames(distinctBuyerNames,
					favNames);

			Collections.sort(combinedNames);

			return combinedNames;
		}

		protected void onPostExecute(List<String> names) {
			populateListViewWithNames(names);
			checkSelectedNamesOnListView();
			updateSelectionForSelectAllCheckBox();

		}

	}

}
