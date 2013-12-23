package com.msafiullah.splitbill.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DbHelper extends SQLiteOpenHelper {
	
	DbHelper(Context context) {		
		super(context, DbDefinition.DATABASE_NAME, null, DbDefinition.DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DbDefinition.TempItemTable.CREATE_TABLE_STATEMENT);
		database.execSQL(DbDefinition.TempBuyerTable.CREATE_TABLE_STATEMENT);
	}

	// Method is called during an upgrade of the database
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(DbDefinition.DATABASE_NAME, "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS "
				+ DbDefinition.TempItemTable.TABLE_NAME);
		database.execSQL("DROP TABLE IF EXISTS "
				+ DbDefinition.TempBuyerTable.TABLE_NAME);
		onCreate(database);
	}

}
