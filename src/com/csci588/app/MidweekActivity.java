package com.csci588.app;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MidweekActivity extends Activity {

	/*called when activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.midweek);

		TextView tV = (TextView) this.findViewById(R.id.leagueName);
		tV.setText("Fantasy Football League");

		tV = (TextView) this.findViewById(R.id.myTeamName);
		tV.setText("Joe's Team");

		tV = (TextView) this.findViewById(R.id.myTeamRecord);
		tV.setText("8-0-1");

		tV = (TextView) this.findViewById(R.id.myTeamRank);
		tV.setText("1 of 12");

		tV = (TextView) this.findViewById(R.id.vsTeamName);
		tV.setText("The Best");

		tV = (TextView) this.findViewById(R.id.alert);
		tV.setText("Frank Gore Probable");

		tabs = (TabHost)this.findViewById(R.id.stat_tabhost);
		tabs.setup();

		DatabaseHelper dbHelp = GamedayActivity.getDbHelp();

		TopPerformerList tpl = new TopPerformerList(this);
		String query = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName, NFL_PLAYERS._id FROM NFL_PLAYERS";
	 	setupTab(tpl.createPerformerList(query, dbHelp,false), "ALL");
	 	query = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName, NFL_PLAYERS._id FROM NFL_PLAYERS where position_id = 0";
		setupTab(tpl.createPerformerList(query, dbHelp,false), "QB");
		query = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName, NFL_PLAYERS._id FROM NFL_PLAYERS where position_id = 2";
		setupTab(tpl.createPerformerList(query, dbHelp,false), "RB");
		query = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName, NFL_PLAYERS._id FROM NFL_PLAYERS where position_id = 1";
		setupTab(tpl.createPerformerList(query, dbHelp,false), "WR");
		query = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName, NFL_PLAYERS._id FROM NFL_PLAYERS where position_id = 3";
		setupTab(tpl.createPerformerList(query, dbHelp,false), "TE");
		query = "SELECT NFL_PLAYERS.fName, NFL_PLAYERS.lName, NFL_PLAYERS._id FROM NFL_PLAYERS where position_id = 4";
		setupTab(tpl.createPerformerList(query, dbHelp,false), "K");

		final ScrollView sv = (ScrollView) this.findViewById(R.id.scrollViewMidweek);
		sv.post(new Runnable() {            
		    public void run() {
		           sv.fullScroll(View.FOCUS_UP);              
		    }
		});
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
	
	private TabHost tabs;
	
}
