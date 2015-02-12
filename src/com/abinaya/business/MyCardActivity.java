package com.abinaya.business;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCardActivity extends Activity implements OnClickListener {
	EditText message;
	Button save;
	String card_value;

	private NfcAdapter adapter;
	private TextView text;
	private NdefMessage ndefMessage;
	public static long sqliteValue;
	private ArrayList<String> cardList = new ArrayList<String>();
	private ListView ListV;
	RelativeLayout screen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_card);
		ListV = (ListView) findViewById(R.id.List);
		message = (EditText) findViewById(R.id.textView1);
		save = (Button) findViewById(R.id.exchangeBtn);
		save.setOnClickListener(this);

		adapter = NfcAdapter.getDefaultAdapter(this);

		ndefMessage = new NdefMessage(new NdefRecord[] { createRecord(message
				.getText().toString(), Locale.ENGLISH, true) });
	}

	public static NdefRecord createRecord(String text, Locale locale,
			boolean encodeInUtf8) {
		byte[] langBytes = locale.getLanguage().getBytes(
				Charset.forName("US-ASCII"));

		Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset
				.forName("UTF-16");
		byte[] textBytes = text.getBytes(utfEncoding);

		int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		char status = (char) (utfBit + langBytes.length);

		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
				textBytes.length);

		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
				new byte[0], data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_card, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onResume() {
		super.onResume();

		if (adapter != null)
			adapter.enableForegroundNdefPush(this, ndefMessage);
	}

	public void onPause() {
		super.onPause();
		if (adapter != null)
			adapter.disableForegroundNdefPush(this);
	}

	public void onClick(View view) {
		if (view == save) {
			card_value = message.getText().toString();
			addCard(card_value);

		}

	}

	public void addCard(String card) {
		SQLiteHelper helper = new SQLiteHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(SQLiteHelper.card_key, card_value);
		sqliteValue = db.insert(SQLiteHelper.table_name, null, contentValues);

	}

}
