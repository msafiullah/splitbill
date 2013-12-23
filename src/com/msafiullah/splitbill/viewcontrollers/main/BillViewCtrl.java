package com.msafiullah.splitbill.viewcontrollers.main;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.dataaccess.PreferenceAdapter;
import com.msafiullah.splitbill.dataaccess.TempDbAdapter;
import com.msafiullah.splitbill.dialog.SelectBuyersDialog;
import com.msafiullah.splitbill.entity.BillItemDTO;
import com.msafiullah.splitbill.listeners.BillItemEventListener;
import com.msafiullah.splitbill.listeners.OnBuyersSelectedListener;
import com.msafiullah.splitbill.listeners.OnProgressViewReachedListener;
import com.msafiullah.splitbill.view.CustomScrollView;
import com.msafiullah.splitbill.viewcontrollers.item.BillItem;
import com.msafiullah.splitbill.viewcontrollers.item.TotalDisplayItem;

public class BillViewCtrl extends AbstractBillViewDbCtrl implements
		BillViewEventListener {

	// private static final String tag = "BillViewCtrl";

	private final ArrayList<BillItem> _billItems;

	private final LinearLayout _billLayout;

	private final TotalDisplayItem _totalItem;

	private final View _progressView;
	private final CustomScrollView _scrollView;

	private final PreferenceAdapter _prefAdapter;

	private boolean _isBillViewLosingFocus = false;

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	public BillViewCtrl(View view) {

		_billItems = new ArrayList<BillItem>();

		_billLayout = (LinearLayout) view.findViewById(R.id.layoutBill);

		_totalItem = new TotalDisplayItem(
				(TextView) view.findViewById(R.id.tvTotal));

		_progressView = View.inflate(_billLayout.getContext(),
				R.layout.circular_loading_item, null);
		_billLayout.addView(_progressView);

		_scrollView = (CustomScrollView) view.findViewById(R.id.scrollViewBill);
		_scrollView
				.setOnProgressViewReachedListener(new OnProgressViewReachedListener() {
					@Override
					public void onProgressViewReached() {
						onScrollViewEndReached();
					}
				});

		setDbAdapter(new TempDbAdapter(view.getContext()
				.getApplicationContext()));
		_prefAdapter = new PreferenceAdapter(view.getContext());

	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	private void retrieveNextBatchOfBillItemsFromTempRecordAsynchronously() {
		int lastItemID = -1;
		try {
			lastItemID = getLastBillItem().getItemID();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}

		retrieveNextBatchOfBillItemsFromTempRecordAsynchronously(lastItemID);
	}

	private boolean haveMoreItemsToLoadFromTempRecord() {
		return getNumItemsInTempRecord() > _billItems.size();
	}

	private void scrollUp() {
		_scrollView.fullScroll(CustomScrollView.FOCUS_UP);
	}

	private void removeProgressView() {
		_billLayout.removeView(_progressView);
	}

	private void moveProgressViewToEnd() {
		removeProgressView();
		_billLayout.addView(_progressView);
	}

	private void updateBillTotal(float priceChange) {
		_totalItem.addToTotal(priceChange);
	}

	/**
	 * ONLY CALLED when user clicks on the add buyer button while he has inputed
	 * nothing on the buyer edit text.
	 */
	private void showSelectBuyersDialog(final BillItem billItem) {
		new SelectBuyersDialog(_billLayout.getContext(), billItem.getBuyers(),
				new OnBuyersSelectedListener() {
					@Override
					public void onBuyerSelectionCompleted(BillItem nullItem,
							List<String> selectedNames) {
						onBillItemBuyersSelectionCompleted(billItem,
								selectedNames);
					}
				}).show();
	}

	private void onBillItemBuyersSelectionCompleted(BillItem billItem,
			List<String> selectedNames) {

		List<String> unselectedExistingBuyers = filterUnselectedExistingBuyers(
				billItem.getBuyers(), selectedNames);
		if (!unselectedExistingBuyers.isEmpty()) {
			billItem.removeBuyers(unselectedExistingBuyers);
		}

		List<String> newlySelectedBuyers = filterNewlySelectedBuyers(
				billItem.getBuyers(), selectedNames);
		if (!newlySelectedBuyers.isEmpty()) {
			billItem.addBuyers(newlySelectedBuyers);
		}

	}

	private List<String> filterUnselectedExistingBuyers(
			List<String> allExistingBuyers, List<String> allSelectedBuyers) {
		List<String> unselectedExistingBuyers = new ArrayList<String>();
		unselectedExistingBuyers.addAll(allExistingBuyers); // copy of all
															// existing buyers
		unselectedExistingBuyers.removeAll(allSelectedBuyers); // obtain
																// existing
																// buyers that
																// user
																// unselected
		return unselectedExistingBuyers;
	}

	private List<String> filterNewlySelectedBuyers(
			List<String> allExistingBuyers, List<String> allSelectedBuyers) {
		List<String> newlySelectedBuyers = new ArrayList<String>();
		newlySelectedBuyers.addAll(allSelectedBuyers);
		newlySelectedBuyers.removeAll(allExistingBuyers);
		return newlySelectedBuyers;
	}

	private void removeBillItem(BillItem billItem) {
		deleteBillItemAsynchronously(billItem);
		_billLayout.removeView(billItem.getView());
		_billItems.remove(billItem);
	}

	private int nextSerialNo() {
		return _billItems.size() + 1;
	}

	private void addNewEntryBillItem() {
		BillItem entryBillItem = new BillItem(_billLayout.getContext(),
				nextSerialNo());
		addBillItem(entryBillItem);
	}

	private void initBillItemEventListener(BillItem billItemToInit) {

		billItemToInit.setBillItemEventListener(_billItemEventListener);

	}

	private void addBillItem(BillItem billItem) {
		initBillItemEventListener(billItem);
		_billItems.add(billItem);
		_billLayout.addView(billItem.getView(), getLayoutParamsForBillItem());
	}

	private LayoutParams getLayoutParamsForBillItem() {
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(11, 5, 11, 4);

		return params;
	}

	private void fillEntryBillItemWithData(float price, List<String> buyers) {
		BillItem entryBillItem = null;

		try {
			entryBillItem = getExistingEntryBillItem();
		} catch (NoSuchElementException e) {
			addNewEntryBillItem();
			entryBillItem = getExistingEntryBillItem();
		}

		// the following two methods will trigger their respective listeners
		// which gives rise to a chain of method calls
		entryBillItem.setPrice(price);
		entryBillItem.addBuyers(buyers);

	}

	private BillItem getLastBillItem() throws NoSuchElementException {
		if (!_billItems.isEmpty()) {
			return _billItems.get(_billItems.size() - 1);
		} else {
			throw new NoSuchElementException("No Last Bill Item found.");
		}
	}

	private BillItem getExistingEntryBillItem() throws NoSuchElementException {
		BillItem lastBillItem = _billItems.get(_billItems.size() - 1);
		if (isEntryBillItem(lastBillItem)) {
			return lastBillItem;
		} else {
			throw new NoSuchElementException(
					"No existing Entry Bill Item found.");
		}
	}

	private boolean isEntryBillItem(BillItem billItem) {
		if (_billItems.get(_billItems.size() - 1).equals(billItem)
				&& billItem.isEmpty()) {
			return true;
		}
		return false;
	}

	private void clearBillLayout() {
		_billLayout.removeAllViews();
	}

	private void updateBillItemsSerialNo(int startIndex) {
		ListIterator<BillItem> i = _billItems.listIterator(startIndex);
		for (int serialNo = startIndex + 1; i.hasNext(); serialNo++) {
			i.next().setSerialNo(serialNo);
		}
	}

	/**
	 * Forces this view to trigger on focus changed listener. In the case where
	 * the last focused view is a edit text with user input, this will trigger
	 * the relevant listeners that allows for saving or deleting of bill item
	 * data.
	 */
	private void clearFocusOnTheViewWithFocus() {
		try {
			_billLayout.findFocus().clearFocus();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
	}

	public void resetBill() {
		_prefAdapter.resetTempBillData();
		resetTempRecord();

		clearBillLayout();
		_billItems.clear();
		_totalItem.reset();
		BillItem.reset();

	}

	public void saveBillTotal() {
		_prefAdapter.saveBillTotal(_totalItem.getTotal());
	}

	@Override
	public void onScrollViewEndReached() {
		onRetrieveNextBatchOfItemsFromTempRecordEvent();
	}

	@Override
	public void onBeforeBillViewFocusLost() {
		_isBillViewLosingFocus = true;
		// forces the view with focus to trigger the on focus changed listener
		clearFocusOnTheViewWithFocus();
	}

	@Override
	public void onEntryItemIsUsedUp(BillItem item) {
		if (!haveMoreItemsToLoadFromTempRecord()) {
			addNewEntryBillItem();
		}
	}

	@Override
	protected void onBillItemCountAndTotalRetrievedFromTempRecord(
			int numBillItemsInTempRecord, float billTotal) {
		// TODO no need to retrieve item count
		_totalItem.setTotal(billTotal);

		if (haveMoreItemsToLoadFromTempRecord()) {
			onRetrieveNextBatchOfItemsFromTempRecordEvent();
		} else {
			onNoMoreItemsToLoadFromTempRecord();
		}
	}

	@Override
	protected void onNextBatchOfBillItemsRetrievedFromTempRecord(
			List<BillItemDTO> nextBatchOfBillItems) {

		boolean isFirstBatch = _billItems.isEmpty();

		for (BillItemDTO eachItem : nextBatchOfBillItems) {
			BillItem billItem = new BillItem(_billLayout.getContext(),
					eachItem.getItemID(), nextSerialNo(), eachItem.getPrice(),
					eachItem.getBuyers());
			addBillItem(billItem);
		}

		if (!haveMoreItemsToLoadFromTempRecord()) {
			onNoMoreItemsToLoadFromTempRecord();
		} else {
			moveProgressViewToEnd();
		}

		if (isFirstBatch) {
			scrollUp();
		}

		onRetrieveNextBatchOfItemsFromTempRecordEvent();// TODO why is this
														// functuion down here?

	}

	@Override
	protected void onNoMoreItemsToLoadFromTempRecord() {
		removeProgressView();
		addNewEntryBillItem();
	}

	private BillItemEventListener _billItemEventListener = new BillItemEventListener() {

		@Override
		public void onPriceChanged(BillItem billItem, float priceChange) {
			if (_isBillViewLosingFocus) {
				// transact with db synchronously
				updateOrInsertBillItemPrice(billItem);
			} else {
				// transact with db asynchronously
				updateOrInsertBillItemPriceAsynchronously(billItem);

			}
			updateBillTotal(priceChange);
		}

		@Override
		public void onBuyerDeleted(BillItem billItem, String deletedBuyer) {
			deleteBillItemBuyerAsynchronously(billItem, deletedBuyer);
		}

		@Override
		public void onBuyersAdded(BillItem billItem, List<String> addedBuyers) {
			if (_isBillViewLosingFocus) {
				// transact with db synchronously
				insertBillItemBuyers(billItem, addedBuyers);
			} else {
				// transact with db asynchronously
				insertBillItemBuyersAsynchronously(billItem, addedBuyers);
			}
		}

		@Override
		public void onBecomesEmpty(BillItem billItem) {
			int billItemIndex = _billItems.indexOf(billItem);
			if (!isEntryBillItem(billItem)) {
				removeBillItem(billItem);
				updateBillItemsSerialNo(billItemIndex);
			}
		}

		@Override
		public void onContentChanged(BillItem item) {
			if (getLastBillItem().equals(item)) {
				onEntryItemIsUsedUp(item);
			}
		}

		@Override
		public void onDelete(BillItem billItemToDelete) {
			updateBillTotal(billItemToDelete.getPrice() * -1);
			int billItemIndex = _billItems.indexOf(billItemToDelete);
			removeBillItem(billItemToDelete);
			updateBillItemsSerialNo(billItemIndex);

			onRetrieveNextBatchOfItemsFromTempRecordEvent();
		}

		@Override
		public void onDuplicate(BillItem billItemToDuplicate) {
			fillEntryBillItemWithData(billItemToDuplicate.getPrice(),
					billItemToDuplicate.getBuyers());
			if (haveMoreItemsToLoadFromTempRecord()) {
				moveProgressViewToEnd();
			}
		}

		@Override
		public void onShowSelectBuyersDialogEvent(BillItem item) {
			showSelectBuyersDialog(item);
		}

	};

	private boolean isProgressViewWithinScrollViewWindow() {
		if (_progressView != null) {
			Rect scrollViewBounds = new Rect();
			_scrollView.getDrawingRect(scrollViewBounds);
			Rect progressViewBounds = new Rect();
			_progressView.getHitRect(progressViewBounds);
			if (scrollViewBounds.intersect(progressViewBounds)) {
				return true;
			}
		}
		return false;
	}

	private void onRetrieveNextBatchOfItemsFromTempRecordEvent() {
		if (!isRetrievingNextBatchOfBillItemsFromTempRecord()
				&& haveMoreItemsToLoadFromTempRecord()
				&& isProgressViewWithinScrollViewWindow()) {
			retrieveNextBatchOfBillItemsFromTempRecordAsynchronously();
		}
	}

}
