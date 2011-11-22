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

public class LeagueActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.league_standings);
		
		// Populate league standings
		LeagueTeamList ltl = new LeagueTeamList(this);
		// get user name, wins, losses, ties
		String query = "select username, wins, losses, ties, managers._id from managers,records where managers._id = records._id order by wins DESC";
		ViewGroup parent = (ViewGroup) findViewById(R.id.standings);
		ltl.createTeamList(parent, query);
		
		
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
