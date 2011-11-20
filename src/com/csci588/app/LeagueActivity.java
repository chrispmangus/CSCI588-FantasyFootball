package com.csci588.app;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.view.ViewGroup;




public class LeagueActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.league_standings);
		
		//DatabaseHelper dbHelp = GamedayActivity.getDbHelp();
		
		// Temporary Dummy DATA
		ViewGroup parent = (ViewGroup) findViewById(R.id.standings);
		//LayoutInflater lf = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.league_team, null);
		TextView tv = (TextView) v.findViewById(R.id.teamRank);
		tv.setText("1");
		tv = (TextView) v.findViewById(R.id.leagueTeamName);
		tv.setText("MyTeam");
		tv = (TextView) v.findViewById(R.id.teamRecord);
		tv.setText("9-1-0");
		tv = (TextView) v.findViewById(R.id.teamPoints);
		tv.setText("998.98");
		parent.addView(v);
		
		v = LayoutInflater.from(getBaseContext()).inflate(R.layout.league_team, null);
		tv = (TextView) v.findViewById(R.id.teamRank);
		tv.setText("2");
		tv = (TextView) v.findViewById(R.id.leagueTeamName);
		tv.setText("TeamA");
		tv = (TextView) v.findViewById(R.id.teamRecord);
		tv.setText("7-3-0");
		tv = (TextView) v.findViewById(R.id.teamPoints);
		tv.setText("944.12");
		parent.addView(v);
		
		v = LayoutInflater.from(getBaseContext()).inflate(R.layout.league_team, null);
		tv = (TextView) v.findViewById(R.id.teamRank);
		tv.setText("3");
		tv = (TextView) v.findViewById(R.id.leagueTeamName);
		tv.setText("TeamB");
		tv = (TextView) v.findViewById(R.id.teamRecord);
		tv.setText("6-4-0");
		tv = (TextView) v.findViewById(R.id.teamPoints);
		tv.setText("925.12");
		parent.addView(v);
		
		v = LayoutInflater.from(getBaseContext()).inflate(R.layout.league_team, null);
		tv = (TextView) v.findViewById(R.id.teamRank);
		tv.setText("4");
		tv = (TextView) v.findViewById(R.id.leagueTeamName);
		tv.setText("TeamC");
		tv = (TextView) v.findViewById(R.id.teamRecord);
		tv.setText("6-4-0");
		tv = (TextView) v.findViewById(R.id.teamPoints);
		tv.setText("844.12");
		parent.addView(v);
		
		v = LayoutInflater.from(getBaseContext()).inflate(R.layout.league_team, null);
		tv = (TextView) v.findViewById(R.id.teamRank);
		tv.setText("5");
		tv = (TextView) v.findViewById(R.id.leagueTeamName);
		tv.setText("TeamD");
		tv = (TextView) v.findViewById(R.id.teamRecord);
		tv.setText("5-5-0");
		tv = (TextView) v.findViewById(R.id.teamPoints);
		tv.setText("978.85");
		parent.addView(v);
		
		v = LayoutInflater.from(getBaseContext()).inflate(R.layout.league_team, null);
		tv = (TextView) v.findViewById(R.id.teamRank);
		tv.setText("6");
		tv = (TextView) v.findViewById(R.id.leagueTeamName);
		tv.setText("TeamE");
		tv = (TextView) v.findViewById(R.id.teamRecord);
		tv.setText("5-5-0");
		tv = (TextView) v.findViewById(R.id.teamPoints);
		tv.setText("926.26");
		parent.addView(v);
		
		v = LayoutInflater.from(getBaseContext()).inflate(R.layout.league_team, null);
		tv = (TextView) v.findViewById(R.id.teamRank);
		tv.setText("7");
		tv = (TextView) v.findViewById(R.id.leagueTeamName);
		tv.setText("Teamf");
		tv = (TextView) v.findViewById(R.id.teamRecord);
		tv.setText("3-7-0");
		tv = (TextView) v.findViewById(R.id.teamPoints);
		tv.setText("785.67");
		parent.addView(v);
		
		v = LayoutInflater.from(getBaseContext()).inflate(R.layout.league_team,null);
		tv = (TextView) v.findViewById(R.id.teamRank);
		tv.setText("8");
		tv = (TextView) v.findViewById(R.id.leagueTeamName);
		tv.setText("TeamG");
		tv = (TextView) v.findViewById(R.id.teamRecord);
		tv.setText("1-9-0");
		tv = (TextView) v.findViewById(R.id.teamPoints);
		tv.setText("754.12");
		parent.addView(v);

		// scrooooollllll
		final ScrollView sv = (ScrollView) this.findViewById(R.id.standingsScroll);
		sv.post(new Runnable() {            
		    public void run() {
		           sv.fullScroll(View.FOCUS_UP);              
		    }
		});
		
		// Lineup Button
		final Button myLineup = (Button) findViewById(R.id.viewLineup);
		myLineup.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), LineupActivity.class);
				startActivityForResult(myIntent,0);
			}
		});
		
	}
	
	
		
}
