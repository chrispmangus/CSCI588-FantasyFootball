package com.csci588.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class GamedayActivity extends Activity {
	
	/*called when activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameday);
		
		/* Necessary for underlining TOP PERFORMER textview */
		TextView tV = (TextView) this.findViewById(R.id.topPerformers);
		SpannableString content = new SpannableString("TOP PERFORMERS");
		content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
		tV.setText(content);
		
		tV = (TextView) this.findViewById(R.id.leagueName);
		tV.setText(new SpannableString("Fantasy Football League"));
		
		tV = (TextView) this.findViewById(R.id.myTeamName);
		tV.setText(new SpannableString("Big Ballers"));
		
		tV = (TextView) this.findViewById(R.id.myTeamScore);
		tV.setText(new SpannableString("102.4"));
		
		tV = (TextView) this.findViewById(R.id.myTeamTime);
		tV.setText(new SpannableString("20 min"));
		
		tV = (TextView) this.findViewById(R.id.vsTeamName);
		tV.setText(new SpannableString("The Best"));
		
		tV = (TextView) this.findViewById(R.id.vsTeamScore);
		tV.setText(new SpannableString("76.2"));
		
		tV = (TextView) this.findViewById(R.id.vsTeamTime);
		tV.setText(new SpannableString("60 min"));
		
		tV = (TextView) this.findViewById(R.id.myTopName);
		tV.setText(new SpannableString("Adrian Peterson"));
		tV = (TextView) this.findViewById(R.id.myTopScore);
		tV.setText(new SpannableString("22.2"));
		tV = (TextView) this.findViewById(R.id.myTopTime);
		tV.setText(new SpannableString("10 min"));
		
		tV = (TextView) this.findViewById(R.id.vsTopName);
		tV.setText(new SpannableString("Ben Roethlisberger"));
		tV = (TextView) this.findViewById(R.id.vsTopScore);
		tV.setText(new SpannableString("20.1"));
		tV = (TextView) this.findViewById(R.id.vsTopTime);
		tV.setText(new SpannableString("20 min"));
	
	}


}