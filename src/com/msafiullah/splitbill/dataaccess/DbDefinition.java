package com.msafiullah.splitbill.dataaccess;

import android.content.ContentValues;

final class DbDefinition {
	final static String DATABASE_NAME = "SplitBillDB";
	final static int DATABASE_VERSION = 13;

	// Temp Item database table
	final static class TempItemTable {
		final static String TABLE_NAME = "TempItem";
		final static String KEY_ROWID = "_id";
		final static String KEY_ITEM_ID = "itemID";
		final static String KEY_ITEM_PRICE = "price";

		final static String CREATE_TABLE_STATEMENT = "create table "
				+ TABLE_NAME + " (" + KEY_ROWID
				+ " integer primary key autoincrement, " + KEY_ITEM_ID
				+ " integer not null, " + KEY_ITEM_PRICE + " text not null);";

		final static ContentValues getContentValues(int itemID, float itemPrice) {
			ContentValues values = new ContentValues();
			values.put(KEY_ITEM_ID, itemID);
			values.put(KEY_ITEM_PRICE, itemPrice);
			return values;
		}

		final static ContentValues getUpdatePriceContentValues(float itemPrice) {
			ContentValues values = new ContentValues();
			values.put(KEY_ITEM_PRICE, itemPrice);
			return values;
		}
	}

	// Temp Buyer database table
	final static class TempBuyerTable {
		final static String TABLE_NAME = "TempBuyer";
		final static String KEY_ROWID = "_id";
		final static String KEY_ITEM_ID = "itemID";
		final static String KEY_BUYER = "buyer";
		final static String ALIAS_BUYER_COUNT = "buyerCount";
		final static String ALIAS_BUYER_STRING = "buyerString";

		final static String CREATE_TABLE_STATEMENT = "create table "
				+ TABLE_NAME + " (" + KEY_ROWID
				+ " integer primary key autoincrement, " + KEY_ITEM_ID
				+ " integer not null, " + KEY_BUYER + " text not null);";

		final static ContentValues getContentValues(int itemID, String buyer) {
			ContentValues values = new ContentValues();
			values.put(KEY_ITEM_ID, itemID);
			values.put(KEY_BUYER, buyer);
			return values;
		}
	}

}
