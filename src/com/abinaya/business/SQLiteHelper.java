package com.abinaya.business;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "cardStore";
	public static final String id_key = "id";
	public static final String card_key = "msg";
	public static final String table_name = "cards";

	public SQLiteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + table_name + "("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "msg TEXT)";
		db.execSQL(CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS message");
		this.onCreate(db);
	}

	public String getCards() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + table_name + "", null);
		// String str=c.getColumnName(1);
		String str = c.getString(1);
		return str;

	}
}
