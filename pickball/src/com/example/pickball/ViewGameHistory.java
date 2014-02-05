package com.example.pickball;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

public class ViewGameHistory extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_view_game_history);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.customtitlebar);	
		TextView tvtitle = (TextView)findViewById(R.id.title_text);
		tvtitle.setText(R.string.game_history);
		WebView wvGameHistory = (WebView) findViewById(R.id.wvGameHistory);
		String customHtml = readLastGameHistory();
		try {
			customHtml = URLEncoder.encode(customHtml, "utf-8");
			customHtml = customHtml.replace('+', ' ');
		} catch (UnsupportedEncodingException e) {
		}		
		wvGameHistory.loadData( customHtml,"text/html", "UTF-8");
	}

	private String readLastGameHistory() {

		String ret = "";
		final String FILENAME = "GameHistory.txt";
		try {
			InputStream inputStream = openFileInput(FILENAME);
			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream);
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}
				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e("ViewGameHistory", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("ViewGameHistory", "Can not read file: " + e.toString());
		}
		return ret;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_view_game_history, menu);
		return true;
	}
}
