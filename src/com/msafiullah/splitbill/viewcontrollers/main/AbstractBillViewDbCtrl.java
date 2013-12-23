package com.msafiullah.splitbill.viewcontrollers.main;

import java.util.List;

import android.os.AsyncTask;

import com.msafiullah.splitbill.dataaccess.TempDbAdapter;
import com.msafiullah.splitbill.entity.BillItemDTO;
import com.msafiullah.splitbill.viewcontrollers.item.BillItem;

public abstract class AbstractBillViewDbCtrl {

	private TempDbAdapter _dbAdapter;
	private boolean _isRetrievingNextBatchOfBillItemsFromTempRecord = false;

	protected void setDbAdapter(TempDbAdapter dbAdapter) {
		_dbAdapter = dbAdapter;
	}

	protected boolean isRetrievingNextBatchOfBillItemsFromTempRecord() {
		return _isRetrievingNextBatchOfBillItemsFromTempRecord;
	}

	protected int getNumItemsInTempRecord() {
		return _dbAdapter.getNumItems();
	}
	
	/**
	 * Can't do this asynchronously because if the deleting is slower than the
	 * retrieving of item count and total from db, then we have a problem.
	 */
	protected void resetTempRecord() {
		_dbAdapter.deleteAllItemsAndBuyers();
	}
	
	protected void updateOrInsertBillItemPrice(BillItem billItem) {
		if (!_dbAdapter.updatePrice(billItem.getItemID(), billItem.getPrice())) {
			_dbAdapter.insertItem(billItem.getItemID(), billItem.getPrice());
		}
	}

	protected void updateOrInsertBillItemPriceAsynchronously(BillItem billItem) {
		new UpdateOrInsertBillItemPriceAsyncTask()
				.execute(new BillItem[] { billItem });
	}

	protected void insertBillItemBuyers(BillItem billItem,
			List<String> newBuyers) {
		_dbAdapter.insertBuyers(billItem.getItemID(), newBuyers);
	}

	protected void insertBillItemBuyersAsynchronously(BillItem billItem,
			List<String> newBuyers) {
		new InsertBillItemBuyersAsyncTask(billItem, newBuyers).execute();
	}

	protected void deleteBillItemBuyerAsynchronously(BillItem billItem,
			String buyerToDelete) {
		new DeleteBillItemBuyersAsyncTask(billItem, buyerToDelete).execute();
	}

	protected void deleteBillItemAsynchronously(BillItem billItem) {
		new DeleteBillItemAsyncTask().execute(new BillItem[] { billItem });
	}

	public void retrieveBillItemCountAndTotalFromTempRecordAsynchronously() {
		new RetrieveBillItemCountAndTotalAsyncTask().execute();
	}

	protected void retrieveNextBatchOfBillItemsFromTempRecordAsynchronously(
			int lastItemID) {
		new RetrieveNextBatchOfBillItemsAsyncTask().execute(new Integer[] {
				lastItemID, 3 });
	}

	protected abstract void onBillItemCountAndTotalRetrievedFromTempRecord(
			int numBillItemsInTempRecord, float billTotal);

	protected abstract void onNextBatchOfBillItemsRetrievedFromTempRecord(
			List<BillItemDTO> billItemDTOs);

	protected abstract void onNoMoreItemsToLoadFromTempRecord();

	private class RetrieveBillItemCountAndTotalAsyncTask extends
			AsyncTask<Void, Void, Float[]> {

		@Override
		protected Float[] doInBackground(Void... params) {
			Float[] countAndTotal = new Float[2];
			countAndTotal[0] = (float) _dbAdapter.retrieveItemCount();
			countAndTotal[1] = _dbAdapter.retrieveBillTotal();
			return countAndTotal;
		}

		protected void onPostExecute(Float[] countAndTotal) {
			onBillItemCountAndTotalRetrievedFromTempRecord(
					countAndTotal[0].intValue(), countAndTotal[1].floatValue());
		}

	}

	private class RetrieveNextBatchOfBillItemsAsyncTask extends
			AsyncTask<Integer, Void, List<BillItemDTO>> {

		@Override
		protected List<BillItemDTO> doInBackground(
				Integer... startItemIDAndLimit) {

			_isRetrievingNextBatchOfBillItemsFromTempRecord = true;

			List<BillItemDTO> billItemDTOs = _dbAdapter
					.retrieveLimitedItemsAndBuyers(
							startItemIDAndLimit[0].intValue(),
							startItemIDAndLimit[1].intValue());
			return billItemDTOs;
		}

		protected void onPostExecute(List<BillItemDTO> billItemDTOs) {
			_isRetrievingNextBatchOfBillItemsFromTempRecord = false;

			onNextBatchOfBillItemsRetrievedFromTempRecord(billItemDTOs);
			
		}

	}

	private class UpdateOrInsertBillItemPriceAsyncTask extends
			AsyncTask<BillItem, Void, Void> {

		@Override
		protected Void doInBackground(BillItem... billItem) {
			updateOrInsertBillItemPrice(billItem[0]);
			return null;
		}

	}

	private class InsertBillItemBuyersAsyncTask extends
			AsyncTask<Void, Void, Void> {

		private BillItem _billItem;
		private List<String> _newBuyers;

		private InsertBillItemBuyersAsyncTask(BillItem billItem,
				List<String> newBuyers) {
			_billItem = billItem;
			_newBuyers = newBuyers;
		}

		@Override
		protected Void doInBackground(Void... params) {
			insertBillItemBuyers(_billItem, _newBuyers);
			return null;
		}

	}

	private class DeleteBillItemBuyersAsyncTask extends
			AsyncTask<Void, Void, Void> {

		private BillItem _billItem;
		private String _buyerToDelete;

		private DeleteBillItemBuyersAsyncTask(BillItem billItem,
				String buyerToDelete) {
			_billItem = billItem;
			_buyerToDelete = buyerToDelete;
		}

		@Override
		protected Void doInBackground(Void... params) {
			_dbAdapter.deleteBuyerForItemID(_buyerToDelete,
					_billItem.getItemID());
			return null;
		}

	}

	private class DeleteBillItemAsyncTask extends
			AsyncTask<BillItem, Void, Void> {

		@Override
		protected Void doInBackground(BillItem... billItem) {
			_dbAdapter.deleteItemAndBuyersForItemID(billItem[0].getItemID());
			return null;
		}

	}

}
