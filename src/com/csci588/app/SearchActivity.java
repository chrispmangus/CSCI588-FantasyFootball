package com.csci588.app;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;


public class SearchActivity extends ListActivity {
	
	/*called when activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.roster_layout);
		
		/*String query = "SELECT NFL_PLAYERS._id,NFL_PLAYERS.fName, NFL_PLAYERS.lName FROM NFL_PLAYERS";
		Cursor dataCursor = GamedayActivity.getDbHelp().getQuery(query);
		setListAdapter(new SeachCursorAdapter(this,dataCursor));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);*/
		
		final Intent queryIntent = getIntent();
		final String queryAction = queryIntent.getAction();
		if(Intent.ACTION_SEARCH.equals(queryAction)){
			String searchKeywords = queryIntent.getStringExtra(SearchManager.QUERY);
			String query = "select nfl_players._id,fName,lName, real_stats from nfl_players,nfl_fantasy_stats " +
					"where (lName like '%" + searchKeywords + "%' " + "or fName like '%" + searchKeywords +
					"%') and nfl_players._id = nfl_fantasy_stats._id order by real_stats desc";
			setListAdapter(new SearchCursorAdapter(this,GamedayActivity.getDbHelp().getQuery(query)));
			
		}
	}
	
}
