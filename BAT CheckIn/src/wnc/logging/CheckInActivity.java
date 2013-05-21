package wnc.logging;

import wnc.logging.modules.AppHelper;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CheckInActivity extends Activity {
	AppHelper appHelper = AppHelper.getInstance();
	Context mContext = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().getDecorView().setSystemUiVisibility(
		// View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		setContentView(R.layout.checkin);
		Button yesButton = (Button) findViewById(R.id.checkButt);

		yesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// Intent thankActivity = new Intent(getApplicationContext(),
				// ThankActivity.class);
				// startActivity(thankActivity);
				// finish();
				DoInBackground d = new DoInBackground();
				d.execute();
			}
		});
		TextView textView = (TextView) findViewById(R.id.user);
		textView.setText(getString(R.string.hello) + " " + "User!");
	}

	private class DoInBackground extends AsyncTask<Void, Void, Void> implements
			DialogInterface.OnCancelListener {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... unused) {

			// Client.getInstance().checkIn();
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			finish();
			Intent thankActivity = new Intent(getApplicationContext(),
					ThankActivity.class);
			startActivity(thankActivity);

		}

		@Override
		public void onCancel(DialogInterface dialog) {
			cancel(true);

		}

	}

	@Override
	public void onBackPressed() {

		finish();
		startActivity(new Intent(mContext, NFCReaderActivity.class));
		super.onBackPressed();
	};

}
