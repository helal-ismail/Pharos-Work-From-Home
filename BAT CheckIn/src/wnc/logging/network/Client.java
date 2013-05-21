package wnc.logging.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


import wnc.logging.modules.AppHelper;

public class Client {
	private static Client intance = new Client();
	AppHelper appHelper = AppHelper.getInstance();

	public static Client getInstance() {
		return intance;
	}

	public void checkUser() {
		try {
			System.out.println("map card");
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httpost = new HttpPost(
					"http://wnc.wavenconnect.com/users/auto_map_card?card_id="
							+ appHelper.cardId);
			httpost.setHeader("Content-type", "application/text");

			HttpResponse response = null;

			response = httpclient.execute(httpost);
			System.out.println(response);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream;

				instream = entity.getContent();

				String result = convertStreamToString(instream);
				System.out.println(result);
				instream.close();

			} else {
				appHelper.success = false;
			}

		} catch (IOException e) {

			e.printStackTrace();

		} catch (IllegalStateException e1) {

			e1.printStackTrace();
		}
	}

	public void checkIn() {
		try {
			System.out.println("map card");
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httpost = new HttpPost(
					"http://wnc.wavenconnect.com/users/auto_map_card?user_id="
							+ appHelper.currUser.id + "&card_id="
							+ appHelper.cardId);
			httpost.setHeader("Content-type", "application/text");

			HttpResponse response = null;

			response = httpclient.execute(httpost);
			System.out.println(response);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream;

				instream = entity.getContent();

				String result = convertStreamToString(instream);
				System.out.println(result);
				instream.close();

			} else {
				appHelper.success = false;
			}

		} catch (IOException e) {

			e.printStackTrace();

		} catch (IllegalStateException e1) {

			e1.printStackTrace();
		}
	}

	private String convertStreamToString(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

//	private void convertStringToList(String res) throws Exception {
//		JSONObject obj = new JSONObject(res);
//		String status = obj.optString("status");
//		if (status.equals("ERROR")) {
//			appHelper.success = false;
//		} else {
//			appHelper.success = true;
//		}
//	}
}
