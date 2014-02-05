package com.example.pickball;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

public class VeiwGameHelp extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_veiw_game_help);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.customtitlebar);
		TextView tvtitle = (TextView)findViewById(R.id.title_text);
		tvtitle.setText(R.string.title_activity_veiw_game_help);			
		WebView wvGameHelp = (WebView) findViewById(R.id.wvGameHelp);		
		wvGameHelp.loadUrl("file:///android_asset/index.html"); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_veiw_game_help, menu);
        return true;
    }
}
