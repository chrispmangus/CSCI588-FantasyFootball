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
		
		/*tV = (TextView) this.findViewById(R.id.myTeamName);
		tV.setText("MyTeam");
		
		tV = (TextView) this.findViewById(R.id.myTeamScore);
		tV.setText("103.4");
		
		tV = (TextView) this.findViewById(R.id.myTeamTime);
		tV.setText("10 min");
		
		tV = (TextView) this.findViewById(R.id.vsTeamName);
		tV.setText("Opponent Team");
		
		tV = (TextView) this.findViewById(R.id.vsTeamScore);
		tV.setText("92.1");
		
		tV = (TextView) this.findViewById(R.id.vsTeamTime);
		tV.setText("60 min");*/
	
	}

}
