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
import android.widget.Button;
import android.widget.ScrollView;
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
		
		tabs = (TabHost)this.findViewById(R.id.stat_tabhost);
		tabs.setup();

		
		
		dbHelp = new DatabaseHelper(this);
		try {
			 
        	dbHelp.createDataBase();
 
	 	} catch (IOException ioe) {
	 
	 		throw new Error("Unable to create database");
	 
	 	}
	 
	 	try {
	 
	 		dbHelp.openDataBase();
	 
	 	}catch(SQLException sqle){
	 
	 		throw sqle;
	 
	 	}

	 	TopPerformerList tpl = new TopPerformerList(this);
	 	String query = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName FROM NFL_PLAYERS";
	 	setupTab(tpl.createPerformerList(query, dbHelp), "ALL");
	 	query = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName FROM NFL_PLAYERS where position_id = 0";
		setupTab(tpl.createPerformerList(query, dbHelp), "QB");
		query = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName FROM NFL_PLAYERS where position_id = 2";
		setupTab(tpl.createPerformerList(query, dbHelp), "RB");
		query = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName FROM NFL_PLAYERS where position_id = 1";
		setupTab(tpl.createPerformerList(query, dbHelp), "WR");
		query = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName FROM NFL_PLAYERS where position_id = 3";
		setupTab(tpl.createPerformerList(query, dbHelp), "TE");
		query = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName FROM NFL_PLAYERS where position_id = 4";
		setupTab(tpl.createPerformerList(query, dbHelp), "K");
		
		final ScrollView sv = (ScrollView) this.findViewById(R.id.scrollViewGameDay);
		sv.post(new Runnable() {            
		    public void run() {
		           sv.fullScroll(View.FOCUS_UP);              
		    }
		});
		
		final Button myLineup = (Button) findViewById(R.id.viewLineup);
		myLineup.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), RosterActivity.class);
				startActivityForResult(myIntent,0);
			}
		});
	}
	
	public static DatabaseHelper getDbHelp(){
		return dbHelp;
	}
	
	private void setupTab(final View view, final String tag){
		View tabview = createTabView(tabs.getContext(), tag);
		TabSpec setContent = tabs.newTabSpec(tag).setIndicator(tabview).setContent(new TabContentFactory(){
			public View createTabContent(String tag){ return view;}
		});
		tabs.addTab(setContent);
	}
	
	private static View createTabView(final Context context, final String text){
		View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
		TextView tv = (TextView) view.findViewById(R.id.tabText);
		tv.setText(text);
		return view;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbHelp != null) {
			dbHelp.close();
		}
	}

	private static DatabaseHelper dbHelp;
	private TabHost tabs;
}