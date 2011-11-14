package com.csci588.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

public class GamedayActivity extends Activity {
	
	/*called when activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameday);
		TextView tV = (TextView) this.findViewById(R.id.topPerformers);
		SpannableString content = new SpannableString("TOP PERFORMERS");
		content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
		tV.setText(content);
	
	}

}
