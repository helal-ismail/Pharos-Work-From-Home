package wnc.logging;

import java.io.IOException;
import wnc.logging.modules.AppHelper;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class NFCReaderActivity extends Activity {
	Context mContext = this;
	AppHelper appHelper = AppHelper.getInstance();
	private static NfcAdapter mAdapter;
	private static PendingIntent mPendingIntent;
	private static IntentFilter[] mFilters;
	private static String[][] mTechLists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().getDecorView().setSystemUiVisibility(
		// View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		setContentView(R.layout.nfcreader);
		//
		// final TranslateAnimation moveDowntoUp = new TranslateAnimation(0, 0,
		// 0, -200);
		// moveDowntoUp.setDuration(500);
		// moveDowntoUp.setFillAfter(false);
		// moveDowntoUp.setRepeatCount(-1);
		// moveDowntoUp.setRepeatMode(Animation.REVERSE);
		// ImageView imgA = (ImageView) findViewById(R.id.arrow1);
		// imgA.startAnimation(moveDowntoUp);
		// ImageView imgB = (ImageView) findViewById(R.id.arrow2);
		// imgB.startAnimation(moveDowntoUp);
		// ImageView imgC = (ImageView) findViewById(R.id.arrow3);
		// imgC.startAnimation(moveDowntoUp);
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		if (!mAdapter.isEnabled()) {
			Toast.makeText(getApplicationContext(), "Please,turn NFC on", 2000)
					.show();
		}
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		// IntentFilter ndef = new
		// IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		IntentFilter tag = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] { ndef, tech, tag };
		mTechLists = new String[][] { new String[] {
				MifareClassic.class.getName(), NfcA.class.getName(),
				Ndef.class.getName() } };
		Intent intent = getIntent();
		resolveIntent(intent);

	}

	void resolveIntent(Intent intent) {

		String action = intent.getAction();
		// System.out.println("here home action" + action);
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			// status_Data.setText("Discovered tag with intent: " + intent);
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			// NfcA mfc=NfcA.get(tagFromIntent);

			// MifareClassic mfc = MifareClassic.get(tagFromIntent);
			// Ndef mfc=Ndef.get(tagFromIntent);
			String type = tagFromIntent.getTechList()[0];
			byte[] data = null;
			try {
				if (type.equalsIgnoreCase("android.nfc.tech.NfcA")) {

					NfcA mfc = NfcA.get(tagFromIntent);
					mfc.connect();

					data = mfc.getTag().getId();
				} else if (type.equalsIgnoreCase("android.nfc.tech.NfcB")) {

					NfcB mfc = NfcB.get(tagFromIntent);
					mfc.connect();

					data = mfc.getTag().getId();
				} else if (type.equalsIgnoreCase("android.nfc.tech.NfcC")) {

					Ndef mfc = Ndef.get(tagFromIntent);
					mfc.connect();

					data = mfc.getTag().getId();
				} else if (type
						.equalsIgnoreCase("android.nfc.tech.MifareClassic")) {

					MifareClassic mfc = MifareClassic.get(tagFromIntent);
					mfc.connect();

					data = mfc.getTag().getId();
				} else if (type
						.equalsIgnoreCase("android.nfc.tech.MifareUltralight")) {

					MifareUltralight mfc = MifareUltralight.get(tagFromIntent);

					mfc.connect();

					data = mfc.getTag().getId();
				}
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Error", 2000).show();
			}
			// MifareUltralight mfc=MifareUltralight.get(tagFromIntent);

	
			String cardData = null;

			// auth = mfc.authenticateSectorWithKeyA(0,
			// MifareClassic.KEY_DEFAULT);
			// if (auth) {

			// data = mfc.readBlock(0);
			cardData = appHelper.getHexString(data, data.length)
					.substring(0, 8);
			String cardID = "";
			for (int i = 0; i < 8; i = i + 2) {
				cardID = cardID + cardData.substring(6 - i, 8 - i);
			}
			// System.out.println(cardID);
			appHelper.cardId = cardID;
			DoInBackground d = new DoInBackground();
			d.execute();
			// finish();
			// startActivity(new Intent(
			// mContext,
			// CheckInActivity.class));

		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			// status_Data.setText("Discovered tag with intent: " + intent);
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			MifareClassic mfc = MifareClassic.get(tagFromIntent);
			byte[] data;
			try {
				mfc.connect();
				boolean auth = false;
				String cardData = null;
				auth = mfc.authenticateSectorWithKeyA(0,
						MifareClassic.KEY_DEFAULT);
				if (auth) {

					data = mfc.readBlock(0);
					cardData = appHelper.getHexString(data, data.length)
							.substring(0, 8);
					String cardID = "";
					for (int i = 0; i < 8; i = i + 2) {
						cardID = cardID + cardData.substring(6 - i, 8 - i);
					}
					appHelper.cardId = cardID;
					DoInBackground d = new DoInBackground();
					d.execute();
					//
					// finish();
					// startActivity(new Intent(
					// mContext,
					// CheckInActivity.class));
				} else {
					Toast.makeText(getApplicationContext(),
							"Unable to read Card", 2000).show();
				}

			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Error", 2000).show();
			}
		}

	}

	@Override
	public void onBackPressed() {
		mAdapter.disableForegroundDispatch(this);
		finish();
		super.onBackPressed();
	};

	@Override
	public void onResume() {

		super.onResume();
		mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
				mTechLists);
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
		resolveIntent(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		mAdapter.disableForegroundDispatch(this);
	}

	private class DoInBackground extends AsyncTask<Void, Void, Void> implements
			DialogInterface.OnCancelListener {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... unused) {

			// Client.getInstance().checkUser();
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {

			finish();
			startActivity(new Intent(mContext, CheckInActivity.class));

		}

		@Override
		public void onCancel(DialogInterface dialog) {
			cancel(true);

		}

	}
}
