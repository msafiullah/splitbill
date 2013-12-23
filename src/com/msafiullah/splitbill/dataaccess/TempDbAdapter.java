package com.msafiullah.splitbill.dataaccess;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.msafiullah.splitbill.entity.BillItemDTO;
import com.msafiullah.splitbill.entity.BuyerItemEntity;

public class TempDbAdapter extends DbHelper {

	private static final String tag = "TempItemBuyerDbAdapter";
	private SQLiteDatabase _database;
	private PreferenceAdapter _prefAdapter;

	private int _numItems = 0;

	public TempDbAdapter(Context context) {
		super(context);
		_prefAdapter = new PreferenceAdapter(context);
		_database = null;

		retrieveItemCount();
	}

	private void openWritableDatabase() {
		if (_database == null || !_database.isOpen() || _database.isReadOnly()) {
			_database = getWritableDatabase();
		}
	}

	private void openReadonlyDatabase() {
		if (_database == null || !_database.isOpen()) {
			_database = getWritableDatabase();
		}
	}

	public void close() {
		if (_database != null)
			_database.close();
		super.close();
	}

	/**
	 * Insert entries
	 */
	public synchronized void insertItem(int itemID, float itemPrice) {
		Log.d(tag, "insertItem()");

		openWritableDatabase();
		_database.insert(DbDefinition.TempItemTable.TABLE_NAME, null,
				DbDefinition.TempItemTable.getContentValues(itemID, itemPrice));
		close();

		retrieveItemCount();
	}

	public synchronized void insertBuyers(int itemID, List<String> buyers) {
		Log.d(tag, "insertBuyers()");

		openWritableDatabase();
		for (String eachBuyer : buyers) {
			_database.insert(DbDefinition.TempBuyerTable.TABLE_NAME, null,
					DbDefinition.TempBuyerTable.getContentValues(itemID,
							eachBuyer));
		}

		close();

		retrieveItemCount();
	}

	public synchronized void insertItemAndBuyers(int itemID, float itemPrice,
			List<String> itemBuyers) {
		Log.d(tag, "insertItemAndBuyers()");

		insertItem(itemID, itemPrice);
		insertBuyers(itemID, itemBuyers);
	}

	/**
	 * Update entries
	 */
	public synchronized boolean updatePrice(int itemID, float itemPrice) {
		Log.d(tag, "updatePrice()");

		openWritableDatabase();
		boolean isItemPriceUpdated = _database.update(
				DbDefinition.TempItemTable.TABLE_NAME,
				DbDefinition.TempItemTable
						.getUpdatePriceContentValues(itemPrice),
				DbDefinition.TempItemTable.KEY_ITEM_ID + "=" + itemID, null) > 0;

		close();

		return isItemPriceUpdated;
	}

	/**
	 * Delete entries
	 */
	public synchronized void deleteAllItemsAndBuyers() {
		Log.d(tag, "deleteAllItemsAndBuyers()");

		openWritableDatabase();
		_database.delete(DbDefinition.TempItemTable.TABLE_NAME, null, null);
		_database.delete(DbDefinition.TempBuyerTable.TABLE_NAME, null, null);
		close();

		retrieveItemCount();
	}

	public synchronized void deleteItemAndBuyersForItemID(int itemID) {
		Log.d(tag, "deleteItemAndBuyersForItemID()");

		openWritableDatabase();
		_database.delete(DbDefinition.TempItemTable.TABLE_NAME,
				DbDefinition.TempItemTable.KEY_ITEM_ID + "=" + itemID, null);
		_database.delete(DbDefinition.TempBuyerTable.TABLE_NAME,
				DbDefinition.TempBuyerTable.KEY_ITEM_ID + "=" + itemID, null);
		close();

		retrieveItemCount();
	}

	public synchronized void deleteItemsAndBuyersForMultipleItemIDs(
			List<Integer> itemIDList) {
		Log.d(tag, "deleteItemsAndBuyersForMultipleItemIDs()");

		// generate item id string separated by comma
		String itemIDStr = "";
		for (int eachItemID : itemIDList) {
			itemIDStr = eachItemID + ",";
		}
		itemIDStr = itemIDStr.substring(0, itemIDStr.length() - 1);

		openWritableDatabase();
		// delete all items with matching item IDs
		_database.delete(DbDefinition.TempItemTable.TABLE_NAME,
				DbDefinition.TempItemTable.KEY_ITEM_ID + " IN " + "("
						+ itemIDStr + ")", null);

		// delete all buyers with matching item IDs
		_database.delete(DbDefinition.TempBuyerTable.TABLE_NAME,
				DbDefinition.TempBuyerTable.KEY_ITEM_ID + "=" + "(" + itemIDStr
						+ ")", null);
		close();
	}

	public synchronized void deleteBuyerForItemID(String buyer, int itemID) {
		Log.d(tag, "deleteBuyerForItemID()");

		openWritableDatabase();
		_database.delete(DbDefinition.TempBuyerTable.TABLE_NAME,
				DbDefinition.TempBuyerTable.KEY_ITEM_ID + "=" + itemID
						+ " AND " + DbDefinition.TempBuyerTable.KEY_BUYER
						+ "='" + buyer + "'", null);
		close();

		retrieveItemCount();
	}

	public synchronized void deleteAllBuyersForItemID(int itemID) {
		Log.d(tag, "deleteAllBuyersForItemID()");

		openWritableDatabase();
		_database.delete(DbDefinition.TempBuyerTable.TABLE_NAME,
				DbDefinition.TempBuyerTable.KEY_ITEM_ID + "=" + itemID, null);
		close();

		retrieveItemCount();
	}

	/**
	 * Fetch entries
	 */

	public synchronized List<String> retrieveDistinctBuyers() {
		Log.d(tag, "retrieveAllBuyers()");

		List<String> buyersList = new ArrayList<String>();

		final String selectDistinctBuyers = "SELECT DISTINCT "
				+ DbDefinition.TempBuyerTable.KEY_BUYER + " from "
				+ DbDefinition.TempBuyerTable.TABLE_NAME + " ORDER BY "
				+ DbDefinition.TempBuyerTable.KEY_BUYER + "";

		openReadonlyDatabase();
		final Cursor distinctBuyersCursor = _database.rawQuery(
				selectDistinctBuyers, null);

		if (distinctBuyersCursor != null) {

			// iterate through cursor to add each distinct buyer to buyersList
			distinctBuyersCursor.moveToFirst();
			while (!distinctBuyersCursor.isAfterLast()) {
				buyersList.add(distinctBuyersCursor.getString(0));
				distinctBuyersCursor.moveToNext();
			}

			distinctBuyersCursor.close();
		}
		close();

		return buyersList;
	}

	public synchronized List<BuyerItemEntity> retrieveBuyersAndAmounts() {
		Log.d(tag, "retrieveBuyersAndAmounts()");

		List<BuyerItemEntity> buyerAndAmountList = new ArrayList<BuyerItemEntity>();

		openReadonlyDatabase();

		Cursor cursor = _database.rawQuery(
				constructSelectBuyersAndAmountsStatement(), null);

		if (cursor != null && cursor.getCount() > 0) {
			int totalBuyerCount = cursor.getCount();
			cursor.moveToFirst();
			do {
				final String buyer = cursor.getString(1);
				final String amountsPayableExpressionStr = cursor.getString(2);
				final float buyerGrandTotal = computeBuyersTotalAmountPayable(
						totalBuyerCount, amountsPayableExpressionStr);

				BuyerItemEntity buyerEntity = new BuyerItemEntity(buyer,
						buyerGrandTotal);
				buyerAndAmountList.add(buyerEntity);

				Log.i(tag, buyerEntity.toString());

			} while (cursor.moveToNext());
		}
		cursor.close();
		close();

		return buyerAndAmountList;
	}

	private String constructSelectBuyersAndAmountsStatement() {

		final String selectEveryItemIDWithPriceStatement = constructSelectEveryItemIDWithPriceStatement();

		// ItemID, BuyerCount
		// 0, 2
		// 1, 1
		// 2, 2
		// 3, 1
		final String selectEveryItemIDWithBuyerCount = ""
				+ "SELECT itemID, COUNT(*) AS buyerCount " + "FROM	TempBuyer "
				+ "GROUP BY itemID";

		// ItemID, SerialNo
		// 0, 1
		// 1, 2
		// 2, 3
		// 3, 4

		final String selectEveryItemIDWithSerialNo = ""
				+ "SELECT everyItemIDTempTbl.itemID, COUNT(*) AS serialNo "
				+ "FROM		("
				+ selectEveryItemIDWithPriceStatement
				+ ") everyItemIDTempTbl"
				+ " INNER JOIN	("
				+ selectEveryItemIDWithPriceStatement
				+ ") everyItemIDTempTbl_2 "
				+ "	ON everyItemIDTempTbl.itemID >= everyItemIDTempTbl_2.itemID "
				+ "GROUP BY everyItemIDTempTbl.itemID ";

		// ItemID, SerialNo, Buyer, ItemPrice, BuyerCount
		// 0, 1, BuyerA, 100, 2
		// 0, 1, BuyerB, 100, 2
		// 1, 2, BuyerA, 120, 1
		// 2, 3, BuyerB, 80, 2
		// 2, 3, BuyerC, 80, 2
		// 3, 4, null, 200, 2
		final String forEveryItemIDSelectEachBuyerAndSerialNoAndItemPriceAndBuyerCount = ""
				+ "SELECT everyItemIDTempTbl.itemID, serialNo, TempBuyer.buyer, price, COALESCE(buyerCount, 1) AS buyerCount "
				+ "FROM 		("
				+ selectEveryItemIDWithPriceStatement
				+ ") everyItemIDTempTbl "
				+ "LEFT JOIN	TempBuyer "
				+ "	ON everyItemIDTempTbl.itemID = TempBuyer.itemID "
				+ "LEFT JOIN	("
				+ selectEveryItemIDWithBuyerCount
				+ ") tempTempBuyerCount "
				+ "	ON tempTempBuyerCount.itemID = TempBuyer.itemID "
				+ "LEFT JOIN	("
				+ selectEveryItemIDWithSerialNo
				+ ") tempTempItemSerialNo "
				+ "	ON tempTempItemSerialNo.itemID = everyItemIDTempTbl.itemID ";

		// SerialNo, Buyer, AmountExpression
		// 1, BuyerA, "100/2, 120/1"
		// 2, BuyerB, "100/2, 80/2"
		// 3, BuyerC, "80/2"
		// 4, Item #4, "200/2"
		final String selectEveryBuyerAndHisAmountPayableExpression = ""
				+ "SELECT serialNo, COALESCE(  buyer  , 'ITEM #' || serialNo  ) AS buyerOrSerialNo, "
				+ "group_concat(  price   || '/' ||   buyerCount  , ',') AS amounts "
				+ "FROM	  ("
				+ forEveryItemIDSelectEachBuyerAndSerialNoAndItemPriceAndBuyerCount
				+ ") GROUP BY buyerOrSerialNo "
				+ " ORDER BY COALESCE(  buyer  , 'zzzz'), serialNo ";

		return selectEveryBuyerAndHisAmountPayableExpression;
	}

	private float computeBuyersTotalAmountPayable(int billBuyerCount,
			String amountsPayableExpressionStr) {

		// calculate total amount payable for this buyer
		final String[] amountPayableExpressionArr = amountsPayableExpressionStr
				.split(",");
		float totalAmountPayable = 0;
		for (String eachExpression : amountPayableExpressionArr) {
			final String operandStr[] = eachExpression.split("/");
			final float price = Float.parseFloat(operandStr[0]);
			final int buyerCount = Integer.parseInt(operandStr[1]);
			totalAmountPayable += (price / buyerCount);
		}

		// add extra charges and deduct discounts

		final float discountedTotal = totalAmountPayable
				- _prefAdapter.retrieveDiscountChargeEntity()
						.computeChargePerBuyerForTotal(billBuyerCount,
								totalAmountPayable);
		final float serviceChargedTotal = discountedTotal
				+ _prefAdapter.retrieveServiceChargeEntity()
						.computeChargePerBuyerForTotal(billBuyerCount,
								discountedTotal);
		final float buyerGrandTotal = serviceChargedTotal
				+ _prefAdapter.retrieveGstChargeEntity()
						.computeChargePerBuyerForTotal(billBuyerCount,
								serviceChargedTotal);

		return buyerGrandTotal;
	}

	public synchronized List<BillItemDTO> retrieveLimitedItemsAndBuyers(
			int startItemID, int limit) {
		Log.d(tag, "retrieveLimitedItemsAndBuyers()");

		List<BillItemDTO> list = new ArrayList<BillItemDTO>();

		final String tB = DbDefinition.TempBuyerTable.TABLE_NAME;
		final String colBuyer = DbDefinition.TempBuyerTable.KEY_BUYER;
		final String colPrice = DbDefinition.TempItemTable.KEY_ITEM_PRICE;
		final String colItemID = DbDefinition.TempItemTable.KEY_ITEM_ID;
		final String colStrBuyer = DbDefinition.TempBuyerTable.ALIAS_BUYER_STRING;

		final String selectEveryItemIDWithPriceStatement = constructSelectEveryItemIDWithPriceStatement();

		final String selectLimitedItemsAndBuyersStatement = "Select i."
				+ colItemID + " AS " + colItemID + ",i." + colPrice + " AS "
				+ colPrice + ", group_concat(b." + colBuyer + ",',') AS "
				+ colStrBuyer + " from (" + selectEveryItemIDWithPriceStatement
				+ ") i left join " + tB + " b on i." + colItemID + " = b."
				+ colItemID + " group by i." + colItemID + ", i." + colPrice
				+ " having i." + colItemID + " > " + startItemID
				+ " order by i." + colItemID + " limit " + limit;

		openReadonlyDatabase();
		Cursor cursor = _database.rawQuery(
				selectLimitedItemsAndBuyersStatement, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				int itemID = cursor.getInt(cursor.getColumnIndex(colItemID));
				float price = cursor.getFloat(cursor.getColumnIndex(colPrice));
				String strBuyers = cursor.getString(cursor
						.getColumnIndex(colStrBuyer));
				ArrayList<String> buyers = new ArrayList<String>();
				if (strBuyers != null) {
					String[] buyersArr = strBuyers.split(",");
					for (String eachBuyer : buyersArr) {
						buyers.add(eachBuyer);
					}
				} else {
					buyers = new ArrayList<String>();
				}

				// add it to list
				BillItemDTO itemEntity = new BillItemDTO(itemID, price, buyers);
				list.add(itemEntity);

				Log.i(tag, itemEntity.toString());

			} while (cursor.moveToNext());
		}

		cursor.close();
		close();

		return list;
	}

	public synchronized int retrieveItemCount() {
		Log.d(tag, "fetchItemCount()");

		int count = 0;

		openReadonlyDatabase();

		String selectDistinctItemIDs = constructSelectEveryItemIDStatement();
		String countDistinctItemIDStatement = "SELECT COUNT(DISTINCT itemID) FROM ("
				+ selectDistinctItemIDs + ") tempDistinctItemIDs ";

		Cursor cursor = _database.rawQuery(countDistinctItemIDStatement, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			count = cursor.getInt(0);
		}

		cursor.close();
		close();

		_numItems = count;

		return count;
	}

	private String constructSelectEveryItemIDStatement() {
		final String selectUnionItemIDs = "SELECT "
				+ DbDefinition.TempItemTable.KEY_ITEM_ID + " AS itemID FROM "
				+ DbDefinition.TempItemTable.TABLE_NAME + " UNION SELECT "
				+ DbDefinition.TempBuyerTable.KEY_ITEM_ID + " AS itemid FROM "
				+ DbDefinition.TempBuyerTable.TABLE_NAME;
		final String selectDistinctItemIDs = "SELECT DISTINCT itemID FROM ("
				+ selectUnionItemIDs + ") tempUnionItemIDs ";
		return selectDistinctItemIDs;
	}

	private String constructSelectEveryItemIDWithPriceStatement() {
		final String selectEveryItemIDStatement = constructSelectEveryItemIDStatement();
		final String selectEveryItemIDWithPriceStatement = "select tempDistinctItemIDs.itemID, COALESCE(price, 0) AS price from ("
				+ selectEveryItemIDStatement
				+ ") tempDistinctItemIDs left join TempItem "
				+ " on tempDistinctItemIDs.itemID = TempItem.itemID ";
		return selectEveryItemIDWithPriceStatement;
	}

	public synchronized float retrieveBillTotal() {
		Log.d(tag, "fetchBillTotal()");

		float billTotal = 0.0f;

		openReadonlyDatabase();
		String query = "SELECT SUM("
				+ DbDefinition.TempItemTable.KEY_ITEM_PRICE + ") FROM "
				+ DbDefinition.TempItemTable.TABLE_NAME;
		Cursor cursor = _database.rawQuery(query, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			billTotal = cursor.getFloat(0);
		}

		cursor.close();
		close();

		return billTotal;
	}

	public int getNumItems() {
		return _numItems;
	}

}