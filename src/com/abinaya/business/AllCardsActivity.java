package com.abinaya.business;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AllCardsActivity extends Activity {

	private NfcAdapter adapter;
	private PendingIntent pIntent;
	private IntentFilter[] intentFilters;
	private String[][] nfcList;
	TextView message;
	private AlertDialog.Builder build;
	Button save;
	public static long sqliteValue;
	private ArrayList<String> msgList = new ArrayList<String>();
	private ArrayList<String> cardId = new ArrayList<String>();
	private ListView ListV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_cards);
		ListV = (ListView) findViewById(R.id.List);

		adapter = NfcAdapter.getDefaultAdapter(this);

		pIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndefIntent = new IntentFilter(
				NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndefIntent.addDataType("*/*");
			intentFilters = new IntentFilter[] { ndefIntent };
		} catch (Exception e) {
			Log.e("CardReceive", e.toString());
		}

		nfcList = new String[][] { new String[] { NfcF.class.getName() } };
	}

	@Override
	public void onNewIntent(Intent intent) {
		String action = intent.getAction();
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

		String s = action + "\n\n" + tag.toString();
		Parcelable[] data = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

		if (data != null) {
			try {
				for (int i = 0; i < data.length; i++) {
					NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();
					for (int j = 0; j < recs.length; j++) {
						if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN
								&& Arrays.equals(recs[j].getType(),
										NdefRecord.RTD_TEXT)) {

							byte[] payload = recs[j].getPayload();
							String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8"
									: "UTF-16";
							int langCodeLen = payload[0] & 0077;

							s += ("\n\nNdefMessage["
									+ i
									+ "], NdefRecord["
									+ j
									+ "]:\n\""
									+ new String(payload, langCodeLen + 1,
											payload.length - langCodeLen - 1,
											textEncoding) + "\"");
							addCard(s);

						}

					}
				}
			} catch (Exception e) {
				Log.e("CardReceive", e.toString());
			}

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		display();
	}

	@Override
	public void onPause() {
		super.onPause();

		if (adapter != null)
			adapter.disableForegroundDispatch(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		SQLiteHelper helper = new SQLiteHelper(this);
		helper.close();

	}

	public void display() {
		SQLiteHelper helper = new SQLiteHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();
		cardId.clear();
		msgList.clear();
		Cursor c = db
				.rawQuery("select * from " + SQLiteHelper.table_name, null);

		if (c.moveToFirst()) {
			do {
				cardId.add(c.getString(c.getColumnIndex(SQLiteHelper.id_key)));
				msgList.add(c.getString(c.getColumnIndex(SQLiteHelper.card_key)));
			} while (c.moveToNext());
		}
		if (msgList.isEmpty()) {
			Toast.makeText(getApplicationContext(), "No cards to display",
					Toast.LENGTH_LONG).show();
		}
		ListAdapter disadpt = new ListAdapter(AllCardsActivity.this, msgList);
		ListV.setAdapter(disadpt);
		c.close();
	}

	public void addCard(String card) {
		SQLiteHelper helper = new SQLiteHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(SQLiteHelper.card_key, card);
		sqliteValue = db.insert(SQLiteHelper.table_name, null, contentValues);

	}
}
